package controllers;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.db.app.files.ResourceAssociatedFile;
import models.db.app.files.ResourceAssociatedFileType;
import models.db.log.UserLog;
import models.db.user.Role;
import models.db.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import utils.Utils;
import utils.app.file.validators.AuthenticatedUsersValidator;
import utils.app.page.BreadcrumbElement;
import utils.app.page.Breadcrumbs;
import utils.app.page.FormSubmissionResult;
import utils.app.page.PageSettings;
import utils.auth.exception.UserCannotHaveRoleException;
import utils.auth.models.AuthenticatedUser;
import utils.auth.oauth.Office365OAuthHelper;
import utils.auth.oauth.models.AuthenticationStepResult;
import utils.auth.oauth.models.OAuthUserInfo;
import utils.auth.oauth.models.UserPhoto;
import utils.log.models.UserLoginAction;
import utils.log.models.UserLogoutAction;
import utils.session.SessionManager;
import utils.session.exception.InvalidIDSessionParameterException;
import views.html.auth.login;
import views.html.base.basic_error_page;

public class AuthenticationController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	public static final String ROLE_KEY = "r";
	public static final String USERNAME_KEY = "u";
	public static final String NAME_KEY = "n";
	public static final String NONCE_KEY = "expected_nonce";

	private static final String USER_PHOTO_BASE_PATH = "/users/photos/";
	private static final String USER_PHOTO_FILENAME = "user_photo_";

	@Inject
	private Utils utils;

	@Inject
	private FormFactory ff;

	@Inject
	private Office365OAuthHelper office365_oauth_helper;

	@Inject
	private SessionManager session_manager;

	public Result renderDefaultLogin(Request request, String redirect) {
		if (redirect == null || redirect.trim().isEmpty()) {
			redirect = request.header("referer").get();
		}

		// return redirect(controllers.routes.AuthenticationController.renderLogin(redirect));
		return redirect(controllers.routes.AuthenticationController.loginWithOffice365(redirect));
	}

	public Result renderDefaultLogout(Request request) {
		return redirect(controllers.routes.AuthenticationController.logout());
	}

	public Result loginWithOffice365(Request request, String redirect) {
		// Generate a nonce UUID
		UUID nonce = UUID.randomUUID();

		log.trace("GENERATED NONCE: " + nonce.toString());

		// Add the generated nonce to the user session
		Session session = this.session_manager.setAuthenticatedUserSessionParameter(request, NONCE_KEY, nonce.toString());

		// Redirect to the generated URL (to start the authentication process)
		return redirect(this.office365_oauth_helper.buildOAuthFirstStepURL(request, nonce.toString(), redirect)).withSession(session);
	}

	public CompletionStage<Result> processOffice365OAuthLoginCallback(Request request) {
		try {
			DynamicForm form = this.ff.form().bindFromRequest(request);

			String error = form.get("error");
			if (error != null && !error.trim().isEmpty()) {
				log.trace("ERROR: " + error);
			}
			String error_description = form.get("error_description");
			if (error_description != null && !error_description.trim().isEmpty()) {
				log.trace("ERROR DESCRIPTION: " + error_description);
			}

			if (error != null && !error.trim().isEmpty() && error_description != null && !error_description.trim().isEmpty()) {
				switch (error) {
				case "invalid_request":
					return CompletableFuture.supplyAsync(() -> showError(request, "auth.error." + error, error_description));
				case "consent_required":
					return CompletableFuture.supplyAsync(() -> showError(request, "auth.error." + error, "auth.error.consent_required_explanation"));
				}
			}

			log.trace("SESSION DATA: " + request.session().data().toString());

			String code = form.get("code");
			log.trace("CODE: " + code);
			String id_token = form.get("id_token");
			log.trace("ID TOKEN: " + id_token);
			String state = form.get("state");
			log.trace("STATE: " + state);
			String session_state = form.get("session_state");
			log.trace("SESSION STATE: " + session_state);
			String expected_nonce = this.utils.session_manager.getAuthenticatedUserSessionParameter(request, NONCE_KEY);
			log.trace("EXPECTED NONCE: " + expected_nonce);

			if (id_token == null) {
				return CompletableFuture.supplyAsync(() -> showError(request, "auth.error.title", "auth.error.invalid_data_received"));
			}

			CompletionStage<AuthenticationStepResult> login_callback_response = this.office365_oauth_helper.processLoginCallback(request, code, id_token, expected_nonce, state, session_state);
			CompletionStage<AuthenticationStepResult> oauth_user_info_response = login_callback_response.thenApplyAsync(login_callback_step_result -> processOAuthUserInfo(request, login_callback_step_result));
			CompletionStage<AuthenticationStepResult> user_info_response = oauth_user_info_response.thenCompose(oauth_user_info_step_result -> getUserPhoto(request, oauth_user_info_step_result));
			return user_info_response.thenApplyAsync(user_info_step_result -> loadUserSession(request, user_info_step_result));
		} catch (RuntimeException e) {
			return CompletableFuture.supplyAsync(() -> showError(request, "auth.error.title", e.getMessage()));
		} catch (Exception e) {
			return CompletableFuture.supplyAsync(() -> showError(request, "auth.error.title", e.getMessage()));
		}
	}

	private AuthenticationStepResult processOAuthUserInfo(Request request, AuthenticationStepResult step_result) {
		if (step_result != null) {
			// Check if it's an error
			if (step_result.isError()) {
				return step_result;
			}

			// Check if it's of the expected type
			if (step_result.getResultData() != null && step_result.getResultData() instanceof OAuthUserInfo) {
				// If so, try to keep going by getting the user's data
				User user = User.getUserFromOAuthUserInfo((OAuthUserInfo) step_result.getResultData());
				if (user != null) {
					// A user was retrieved, return it
					return new AuthenticationStepResult(false, null, user);
				}
			}
		}

		// Something unexpected happened, return error
		return new AuthenticationStepResult(true, "auth.error.invalid_data_received", null);
	}

	private CompletionStage<AuthenticationStepResult> getUserPhoto(Request request, AuthenticationStepResult step_result) {
		if (step_result != null && step_result.getResultData() != null && step_result.getResultData() instanceof User) {
			User user = (User) step_result.getResultData();

			OAuthUserInfo oauth_info = user.getOAuthUserInfo();
			if (oauth_info != null) {
				CompletionStage<UserPhoto> user_photo_result = this.office365_oauth_helper.getUserPhoto(request, oauth_info);
				return user_photo_result.thenCompose(image_bytes -> savePhoto(user, image_bytes, step_result));
			}
		}
		return CompletableFuture.supplyAsync(() -> step_result);
	}

	private CompletionStage<AuthenticationStepResult> savePhoto(User user, UserPhoto user_photo, AuthenticationStepResult step_result) {
		if (user_photo != null && user_photo.getImageBytes() != null) {
			String path = USER_PHOTO_BASE_PATH;
			String filename = USER_PHOTO_FILENAME + user.getID() + this.utils.static_content_utils.getExtensionFromContentType(user_photo.getContentType());

			File file = this.utils.static_content_utils.saveFile(user_photo.getImageBytes(), path, filename);
			if (file != null) {
				String file_hash = null;
				try {
					file_hash = this.utils.other_utils.getFileMD5Hash(file);
				} catch (Exception e) {
					log.warn("Unable to calculate file hash: " + e.getMessage());
				}

				deletePreviousUserPhotos(user);

				saveUserPhoto(user, user_photo, path, filename, file, file_hash);
			}
		}

		return CompletableFuture.supplyAsync(() -> step_result);
	}

	private void saveUserPhoto(User user, UserPhoto user_photo, String path, String filename, File file, String file_hash) {
		ResourceAssociatedFile resource = new ResourceAssociatedFile();
		resource.setResourceClass(User.class.getCanonicalName());
		resource.setResourceID(user.getID().toString());
		resource.setFileContentType(user_photo.getContentType());
		resource.setFileName(filename);
		resource.setFileHash(file_hash);
		resource.setFilePath(path + filename);
		resource.setFileSize(new Long(user_photo.getImageBytes().length));
		resource.setFileType(ResourceAssociatedFileType.getByID(ResourceAssociatedFileType.USER_PHOTO));
		resource.setIsPublic(false);
		resource.setRestrictedAccessValidationClass(AuthenticatedUsersValidator.class.getCanonicalName());
		resource.setRecordAccesses(false);
		resource.save();
	}

	private void deletePreviousUserPhotos(User user) {
		if (user != null) {
			List<ResourceAssociatedFile> all_photos = ResourceAssociatedFile.getByResourceAndType(User.class.getCanonicalName(), user.getID().toString(), ResourceAssociatedFileType.USER_PHOTO);
			if (all_photos != null) {
				for (ResourceAssociatedFile photo : all_photos) {
					photo.delete();
				}
			}
		}
	}

	private Result loadUserSession(Request request, AuthenticationStepResult step_result) {

		if (step_result != null) {
			if (step_result.isError()) {
				return showError(request, "auth.error.title", step_result.getErrorMessage());
			}

			if (step_result.getResultData() != null && step_result.getResultData() instanceof User) {
				try {
					User user = (User) step_result.getResultData();

					// Add role as USER (if not present already)
					user.updateRoleAssociation(Role.getByID(Role.USER_ID), true);
					user.save();
					// Get most up-to-date info for user
					user.refresh();

					// Load the user session
					Session session = loadSessionForUser(request, user, null);

					String redirect = null;

					// Get access to OAuth user info
					OAuthUserInfo oauth_user_info = user.getOAuthUserInfo();
					if (oauth_user_info != null) {
						redirect = oauth_user_info.getState();
					}

					// Log user login
					UserLog.log(user, new Date(), new UserLoginAction("OAuth", user.getDefaultRole().getID(), redirect, false));

					// Redirect to referrer (if any)
					if (redirect != null && !redirect.trim().isEmpty() && !this.utils.validation_utils.isValidURL(redirect)) {
						return redirect(redirect).withSession(session);
					}

					// Otherwise, redirect to the backoffice index
					return redirect(controllers.bo.routes.BackofficeUserController.renderBackofficeIndex());

				} catch (InvalidIDSessionParameterException e) {
					return showError(request, "auth.error.title", e.getMessage());
				} catch (UserCannotHaveRoleException e) {
					return showError(request, "auth.error.title", this.utils.l.l(request, "auth.text.user_cannot_have_role") + ": " + e.getMessage());
				}
			} else {
				return showError(request, "auth.error.title", "auth.error.no_user_info_available");
			}
		}

		return showError(request, "auth.error.title", "auth.error.invalid_data_received");
	}

	public Result renderLogin(Request request, String redirect) {
		if (this.utils.session_manager.isAuthenticated(request)) {
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
		}

		Breadcrumbs crumbs = this.utils.breacrumb_helper.getBreadcrumbsWithElements(request, this.utils.l, new BreadcrumbElement(this.utils.l.l(request, "auth.title.login"), request.uri(), true));
		return ok(login.render(request, this.utils, new PageSettings(request, this.utils.l, null, crumbs), redirect));
	}

	public Result submitLogin(Request request) {

		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return redirectLoginAux(request, "general.text.invalid_data_sent");
		}

		String username = form.get("username");
		String password = form.get("password");
		String redirect = form.get("redirect");

		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			return redirectLoginAux(request, "general.text.invalid_data_sent");
		}

		User user = User.getByUsername(username);
		if (user == null) {
			return redirectLoginAux(request, "auth.text.invalid_credentials");
		}

		if (user.authenticate(password)) {
			try {
				// Load the user session
				Session session = loadSessionForUser(request, user, null);

				// Log user login
				UserLog.log(user, new Date(), new UserLoginAction("Local Login", user.getDefaultRole().getID(), redirect, false));

				// Redirect to referrer (if any)
				if (redirect != null && !redirect.trim().isEmpty() && !this.utils.validation_utils.isValidURL(redirect)) {
					return redirect(redirect).withSession(session);
				}

				// Redirect to Profile page
				return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage()).withSession(session);

			} catch (InvalidIDSessionParameterException e) {
				return redirectLoginAux(request, e.getMessage());
			} catch (UserCannotHaveRoleException e) {
				return redirectLoginAux(request, this.utils.l.l(request, "auth.text.user_cannot_have_role") + ": " + e.getMessage());
			}
		} else {
			return redirectLoginAux(request, "auth.text.invalid_credentials");
		}
	}

	private Result redirectLoginAux(Request request, String error) {
		if (error != null) {
			return redirect(controllers.routes.AuthenticationController.renderLogin(null)).flashing(FormSubmissionResult.FLASH_RESULT, error).flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		}
		return redirect(controllers.routes.AuthenticationController.renderLogin(null));
	}

	public Result logout(Request request) {
		// Log user logout
		User user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (user != null) {
			UserLog.log(user, new Date(), new UserLogoutAction());
		}

		// Destroy session
		Http.Session session = this.utils.session_manager.resetSession(request.session());
		// Redirect to Index page
		return redirect(controllers.routes.ApplicationController.index()).withSession(session);
	}

	public Session loadSessionForUser(Request request, User user, Role intended_role) throws InvalidIDSessionParameterException, UserCannotHaveRoleException {
		if (user != null) {
			Role role = null;
			if (intended_role != null) {
				role = intended_role;
			} else {
				role = user.getDefaultRole();
			}

			if (role == null || !user.canHaveRole(role)) {
				throw new UserCannotHaveRoleException(this.utils.l.lto(request, intended_role, "getLabel"));
			}

			Map<String, String> session_parameters = new HashMap<String, String>();
			session_parameters.put(SessionManager.ID, user.getID().toString());
			session_parameters.put(ROLE_KEY, role.getID().toString());
			session_parameters.put(USERNAME_KEY, user.getUsername());
			session_parameters.put(NAME_KEY, user.getDisplayName());

			return this.utils.session_manager.loadUserSession(session_parameters, request.session());
		}

		return request.session();
	}

	public AuthenticatedUser getAuthenticatedUserInfo(Request request) {

		User user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (user != null) {
			Role role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, ROLE_KEY)));
			if (role != null) {
				User admin_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAdminUser(request.session())));
				if (admin_user != null) {
					return new AuthenticatedUser(user, role, true, admin_user);
				}
				return new AuthenticatedUser(user, role, false);
			}
		}

		return null;
	}

	private Result showError(Request request, String title, String message) {
		return internalServerError(basic_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, title), this.utils.l.l(request, message), true));
	}
}
