package controllers.bo;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actions.IsAuthenticated;
import actions.IsAuthenticatedAs;
import controllers.AuthenticationController;
import io.ebean.Ebean;
import io.ebean.PagedList;
import models.db.app.config.ApplicationConfigurationProperty;
import models.db.log.UserLog;
import models.db.sys.JobDescription;
import models.db.user.Role;
import models.db.user.User;
import models.helpers.app.config.ApplicationConfigurationHelper;
import models.helpers.exception.FailsValidationException;
import models.helpers.exception.InvalidFormDataException;
import models.helpers.exception.InvalidModelException;
import models.helpers.user.UserHelper;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.twirl.api.Html;
import utils.Utils;
import utils.app.config.AppConfig;
import utils.app.page.FormSubmissionResult;
import utils.app.page.PageSettings;
import utils.app.page.Pagination;
import utils.auth.exception.UserCannotHaveRoleException;
import utils.log.models.UserImpersonateAction;
import utils.log.models.UserLoginAction;
import utils.log.models.UserResetImpersonateAction;
import utils.search.SearchHelper;
import utils.session.SessionManager;
import utils.session.exception.InvalidIDSessionParameterException;
import utils.session.exception.InvalidRoleDataException;
import views.html.base.basic_delete_form;
import views.html.base.basic_error_message;
import views.html.base.basic_error_page;
import views.html.base.basic_warning_message;
import views.html.system.menu_management;
import views.html.system.system_config;
import views.html.system.system_config_edit;
import views.html.system.system_config_property_form;
import views.html.system.system_jobs;
import views.html.system.system_logs;
import views.html.system.user_management;
import views.html.system.user_management_all_user_logs;
import views.html.system.user_management_edit_user;
import views.html.system.user_management_impersonate_user;
import views.html.system.user_management_search_results;
import views.html.system.user_management_user;
import views.html.system.user_management_user_log;

public class BackofficeSystemController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(BackofficeSystemController.class);

	private static final String SEARCH_TERM_KEY = "search_term";
	private static final String PAGE_KEY = "page";
	private static final int PAGE_SIZE = 5;

	@Inject
	private Utils utils;

	@Inject
	private FormFactory ff;

	@Inject
	private ApplicationConfigurationHelper app_config_helper;

	@Inject
	private SearchHelper search_helper;

	@Inject
	private UserHelper user_helper;

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemIndex(Request request) {
		return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig());
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemConfig(Request request) {
		return ok(system_config.render(request, this.utils, generatePageSettings(request, "system.title.config")));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemConfigEdit(Request request) {
		return renderSystemConfigEditAux(request, null);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitSystemConfigEdit(Request request) {

		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		}

		try {
			Ebean.beginTransaction();

			this.app_config_helper.updateData(AppConfig.getInstance(), form, request);

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.save_success");

		} catch (InvalidFormDataException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (InvalidModelException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (FailsValidationException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderAddSystemConfigPropertyForm(Request request) {
		return ok(system_config_property_form.render(request, this.utils, generatePageSettings(request, "system.title.config_property_form"), null, controllers.bo.routes.BackofficeSystemController.renderAddSystemConfigPropertyForm().toString()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitAddSystemConfigPropertyForm(Request request) {
		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		}

		try {
			Ebean.beginTransaction();

			this.app_config_helper.addProperty(AppConfig.getInstance(), form, request);

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.save_success");

		} catch (InvalidFormDataException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (InvalidModelException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (FailsValidationException e) {
			log.error(e.getMessage());
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return renderSystemConfigEditAux(request, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderEditSystemConfigPropertyForm(Request request, Integer property_id) {
		ApplicationConfigurationProperty property = ApplicationConfigurationProperty.getByID(property_id);
		if (property != null) {
			return ok(system_config_property_form.render(request, this.utils, generatePageSettings(request, "system.title.config_property_form"), property, controllers.bo.routes.BackofficeSystemController.renderEditSystemConfigPropertyForm(property.getID()).toString()));
		}
		return redirect(controllers.bo.routes.BackofficeSystemController.renderAddSystemConfigPropertyForm());
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitEditSystemConfigPropertyForm(Request request, Integer property_id) {
		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.invalid_data_sent").flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		}

		try {
			Ebean.beginTransaction();

			this.app_config_helper.updateProperty(AppConfig.getInstance(), property_id, form, request);

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.save_success");

		} catch (InvalidFormDataException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.invalid_data_sent").flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		} catch (InvalidModelException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.invalid_data_sent").flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		} catch (FailsValidationException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, e.getMessage()).flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, e.getMessage()).flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderDeleteSystemConfigPropertyConfirmation(Request request, Integer property_id) {
		ApplicationConfigurationProperty property = ApplicationConfigurationProperty.getByID(property_id);
		if (property != null) {
			return ok(basic_delete_form.render(request, this.utils, generatePageSettings(request, "system.title.config_property_delete"), controllers.bo.routes.BackofficeSystemController.renderDeleteSystemConfigPropertyConfirmation(property.getID()).toString(), controllers.bo.routes.BackofficeSystemController.renderSystemConfig().toString(), this.utils.l.l(request, "system.title.config_property_delete"), this.utils.l.la(request, "general.text.basic_delete_confirmation", property.getLabel() + ": " + property.getValue())));
		}
		return redirect(controllers.bo.routes.BackofficeSystemController.renderAddSystemConfigPropertyForm());
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitDeleteSystemConfigPropertyConfirmation(Request request, Integer property_id) {
		try {
			Ebean.beginTransaction();

			this.app_config_helper.deleteProperty(AppConfig.getInstance(), property_id, request);

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.save_success");

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return redirect(controllers.bo.routes.BackofficeSystemController.renderSystemConfig()).flashing(FormSubmissionResult.FLASH_RESULT, e.getMessage()).flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		} finally {
			Ebean.endTransaction();
		}
	}

	private Result renderSystemConfigEditAux(Request request, FormSubmissionResult form_result) {
		return ok(system_config_edit.render(request, this.utils, generatePageSettings(request, "system.title.config_edit"), form_result));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemJobs(Request request) {
		return ok(system_jobs.render(request, this.utils, generatePageSettings(request, "system.title.jobs"), JobDescription.getAll()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemLogs(Request request) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.system_logs", request.uri());
			}
		}, true);

		return ok(system_logs.render(request, this.utils, page_settings));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderMenuManagement(Request request) {
		return ok(menu_management.render(request, this.utils, generatePageSettings(request, "system.title.menu_management")));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderUserManagement(Request request) {
		return ok(user_management.render(request, this.utils, generatePageSettings(request, "system.title.user_management"), null, null));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitUserManagement(Request request) {
		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return badRequest(user_management.render(request, this.utils, generatePageSettings(request, "system.title.user_management"), null, basic_error_message.render(this.utils.l.l(request, "general.text.invalid_data_sent"), false)));
		}

		String search_term = form.get(SEARCH_TERM_KEY);
		Integer page = this.utils.type_utils.getIntegerValue(form.get(PAGE_KEY));

		return ok(user_management.render(request, this.utils, generatePageSettings(request, "system.title.user_management"), search_term, generateUserSearchResults(request, search_term, page)));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderAllUserLogs(Request request) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				put("system.title.users_logs", request.uri());
			}
		}, true);

		return ok(user_management_all_user_logs.render(request, this.utils, page_settings));
	}

	@IsAuthenticated()
	public Result resetImpersonateUser(Request request) {
		// Get the authenticated user
		String auth_user_id = this.utils.session_manager.getAuthenticatedUserID(request);

		// Get the admin user from session
		User admin_user = User.getByID(utils.type_utils.getLongValue(utils.session_manager.getAdminUser(request.session())));
		if (admin_user == null) {
			log.warn("User " + auth_user_id + " cannot impersonate-reset because there's no Admin User in session!");
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
		}

		Role admin_role = Role.getByID(Integer.parseInt(Role.ADMIN));
		if (!admin_user.canHaveRole(admin_role)) {
			log.warn("User " + auth_user_id + " cannot impersonate-reset because it does not have ADMIN role!");
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
		}

		Map<String, String> session_parameters = new HashMap<String, String>();
		session_parameters.put(SessionManager.ID, admin_user.getID().toString());
		session_parameters.put(AuthenticationController.ROLE_KEY, admin_role.getID().toString());
		session_parameters.put(AuthenticationController.USERNAME_KEY, admin_user.getUsername());
		session_parameters.put(AuthenticationController.NAME_KEY, admin_user.getDisplayName());

		try {
			Session session = this.utils.session_manager.resetAdminUserMode(session_parameters);
			// Log reset impersonate action
			UserLog.log(admin_user, new Date(), new UserResetImpersonateAction(this.utils.type_utils.getLongValue(auth_user_id)));

			// Redirect to Profile page
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage()).withSession(session);
		} catch (InvalidIDSessionParameterException e) {
			log.warn("Unable to set admin user mode for User " + auth_user_id + ": " + e.getMessage());
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderUser(Request request, Long user_id) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				put("system.title.user", request.uri());
			}
		}, true);
		return ok(user_management_user.render(request, this.utils, page_settings, User.getByID(user_id), authUserCanImpersonateUser(request, user_id)));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderEditUserForm(Request request, Long user_id) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				put("system.title.user", controllers.bo.routes.BackofficeSystemController.renderUser(user_id).toString());
				put("system.title.edit_user", request.uri());
			}
		}, true);
		return ok(user_management_edit_user.render(request, this.utils, page_settings, User.getByID(user_id), Role.getAll(), null));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitEditUserForm(Request request, Long user_id) {
		User user = User.getByID(user_id);

		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return renderEditUserFormAux(request, user, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		}

		try {
			Ebean.beginTransaction();

			this.user_helper.updateData(user, form, request);

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeSystemController.renderUser(user_id)).flashing(FormSubmissionResult.FLASH_RESULT, "general.text.save_success");

		} catch (InvalidFormDataException e) {
			log.error(e.getMessage());
			return renderEditUserFormAux(request, user, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (InvalidModelException e) {
			log.error(e.getMessage());
			return renderEditUserFormAux(request, user, new FormSubmissionResult(request, this.utils.l, false, "general.text.invalid_data_sent", true, form));
		} catch (FailsValidationException e) {
			log.error(e.getMessage());
			return renderEditUserFormAux(request, user, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return renderEditUserFormAux(request, user, new FormSubmissionResult(request, this.utils.l, false, e.getMessage(), true, form));
		} finally {
			Ebean.endTransaction();
		}

	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderImpersonateUserConfirmation(Request request, Long user_id) {

		// Check if the authenticated user can impersonate this user
		boolean can_impersonate = authUserCanImpersonateUser(request, user_id);
		if (!can_impersonate) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.forbidden"), this.utils.l.l(request, "error.text.forbidden"), false));
		}

		// Check if the ID represents a valid user
		User user = User.getByID(user_id);
		if (user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		// Check if the user to be impersonated has active roles
		List<Role> possible_roles = user.getPossibleRoles();
		if (possible_roles == null || possible_roles.isEmpty()) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "auth.text.user_has_no_roles"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				put("system.title.user", controllers.bo.routes.BackofficeSystemController.renderUser(user_id).toString());
				put("system.title.impersonate_user", request.uri());
			}
		}, true);

		return ok(user_management_impersonate_user.render(request, this.utils, page_settings, user));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitImpersonateUserConfirmation(Request request, Long user_id) {

		// Check if the authenticated user can impersonate this user
		boolean can_impersonate = authUserCanImpersonateUser(request, user_id);
		if (!can_impersonate) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.forbidden"), this.utils.l.l(request, "error.text.forbidden"), false));
		}

		// Check if the ID represents a valid user
		User user = User.getByID(user_id);
		if (user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		// Check if the user to be impersonated has active roles
		List<Role> possible_roles = user.getPossibleRoles();
		if (possible_roles == null || possible_roles.isEmpty()) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "auth.text.user_has_no_roles"), false));
		}

		Long authenticated_user_id = this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request));
		User authenticated_user = User.getByID(authenticated_user_id);
		if (authenticated_user_id == null || authenticated_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		try {
			// Load the user session
			Session session = loadImpersonateSessionForUser(request, authenticated_user_id.toString(), user, null);

			// Log impersonate action
			UserLog.log(authenticated_user, new Date(), new UserImpersonateAction(user_id));
			// Log user login
			UserLog.log(user, new Date(), new UserLoginAction("local login", user.getDefaultRole().getID(), "", true));

			// Redirect to Profile page
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage()).withSession(session);

		} catch (InvalidIDSessionParameterException e) {
			e.printStackTrace();
			log.error("ERROR: " + e.getMessage());
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.operation_error") + ": " + e.getMessage(), false));
		} catch (UserCannotHaveRoleException e) {
			e.printStackTrace();
			log.error("ERROR: " + e.getMessage());
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.operation_error") + ": " + e.getMessage(), false));
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderUserLog(Request request, Long user_id) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				put("system.title.user", controllers.bo.routes.BackofficeSystemController.renderUser(user_id).toString());
				put("system.title.user_log", request.uri());
			}
		}, true);

		return ok(user_management_user_log.render(request, this.utils, page_settings, User.getByID(user_id)));
	}

	private Result renderEditUserFormAux(Request request, User user, FormSubmissionResult form_result) {
		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("system.title.user_management", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				if (user != null) {
					put("system.title.user", controllers.bo.routes.BackofficeSystemController.renderUser(user.getID()).toString());
				} else {
					put("system.title.user", controllers.bo.routes.BackofficeSystemController.renderUserManagement().toString());
				}
				put("system.title.edit_user", request.uri());
			}
		}, true);
		return ok(user_management_edit_user.render(request, this.utils, page_settings, user, Role.getAll(), form_result));
	}

	private Html generateUserSearchResults(Request request, String search_term, Integer page) {
		if (search_term == null || search_term.trim().isEmpty()) {
			return basic_error_message.render(this.utils.l.l(request, "search.text.search_term_cannot_be_empty"), false);
		}

		if (page == null) {
			page = 1;
		}

		PagedList<User> results = this.search_helper.performUserSearch(search_term, page, PAGE_SIZE);
		if (results != null) {
			results.loadCount();
			List<User> list = results.getList();
			if (results.getTotalCount() > 0) {
				return user_management_search_results.render(request, this.utils, search_term, new Pagination(results), list);
			}
		}

		return basic_warning_message.render(this.utils.l.l(request, "search.text.no_results_found"), false);
	}

	private boolean authUserCanImpersonateUser(Request request, Long user_id) {
		try {
			return this.utils.session_manager.isAuthenticatedAs(AuthenticationController.ROLE_KEY, Role.ADMIN, request.session());
		} catch (InvalidRoleDataException warning) {
			log.warn(warning.getMessage());
		}
		return false;
	}

	public Session loadImpersonateSessionForUser(Request request, String admin_id, User user, Role intended_role) throws InvalidIDSessionParameterException, UserCannotHaveRoleException {
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
			session_parameters.put(AuthenticationController.ROLE_KEY, role.getID().toString());
			session_parameters.put(AuthenticationController.USERNAME_KEY, user.getUsername());
			session_parameters.put(AuthenticationController.NAME_KEY, user.getDisplayName());

			return this.utils.session_manager.loadUserSessionAsAdmin(admin_id, session_parameters, request);
		}

		return request.session();
	}

	@SuppressWarnings("serial")
	private PageSettings generatePageSettings(Request request, String label) {
		return this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("system.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put(label, request.uri());
			}
		}, true);
	}
}
