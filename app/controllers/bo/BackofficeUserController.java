package controllers.bo;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import actions.IsAuthenticated;
import controllers.AuthenticationController;
import models.db.log.UserLog;
import models.db.user.Role;
import models.db.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import utils.Utils;
import utils.app.page.FormSubmissionResult;
import utils.app.page.PageSettings;
import utils.auth.exception.UserCannotHaveRoleException;
import utils.log.models.UserRoleChangeAction;
import utils.session.exception.InvalidIDSessionParameterException;
import views.html.base.basic_error_page;
import views.html.user.profile;
import views.html.user.role_change;

public class BackofficeUserController extends Controller {

	@Inject
	private Utils utils;

	@Inject
	private FormFactory ff;

	@Inject
	private AuthenticationController auth_controller;

	@IsAuthenticated
	public Result renderBackofficeIndex(Request request) {
		return redirect(controllers.bo.routes.BackofficeUserController.renderBackofficeUserIndex());
	}

	@IsAuthenticated
	public Result renderBackofficeUserIndex(Request request) {
		return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
	}

	@IsAuthenticated
	public Result renderProfilePage(Request request) {
		return ok(profile.render(request, this.utils, generatePageSettings(request, "user.title.profile"), this.auth_controller.getAuthenticatedUserInfo(request)));
	}

	@IsAuthenticated
	public Result renderRoleChange(Request request) {

		User user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		if (!user.hasMultipleRoles()) {
			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage());
		}

		Role current_role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));
		return ok(role_change.render(request, this.utils, generatePageSettings(request, "user.title.change_role"), current_role, user.getPossibleRoles()));
	}

	@IsAuthenticated
	public Result submitRoleChange(Request request) {

		User user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return redirectRoleChangeAux(request, this.utils.l.l(request, "general.text.invalid_data_sent"));
		}

		Role role = Role.getByID(this.utils.type_utils.getIntegerValue(form.get("role")));
		if (role == null) {
			return redirectRoleChangeAux(request, this.utils.l.l(request, "general.text.invalid_data_sent"));
		}

		try {
			// Get previous role
			Role previous_role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));

			// Perform the role change
			Session session = this.auth_controller.loadSessionForUser(request, user, role);

			if (previous_role != null) {
				// Log user role change
				UserLog.log(user, new Date(), new UserRoleChangeAction(previous_role.getID(), role.getID()));
			}

			return redirect(controllers.bo.routes.BackofficeUserController.renderProfilePage()).withSession(session);
		} catch (InvalidIDSessionParameterException e) {
			return redirectRoleChangeAux(request, this.utils.l.l(request, "general.text.invalid_data_sent") + ": " + e.getMessage());
		} catch (UserCannotHaveRoleException e) {
			return redirectRoleChangeAux(request, this.utils.l.l(request, "auth.text.user_cannot_have_role") + ": " + e.getMessage());
		}
	}

	private Result redirectRoleChangeAux(Request request, String error) {
		if (error != null) {
			return redirect(controllers.bo.routes.BackofficeUserController.renderRoleChange()).flashing(FormSubmissionResult.FLASH_RESULT, error).flashing(FormSubmissionResult.FLASH_IS_ERROR, "true");
		}
		return redirect(controllers.bo.routes.BackofficeUserController.renderRoleChange());
	}

	@SuppressWarnings("serial")
	private PageSettings generatePageSettings(Request request, String label) {
		return this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("user.title.index", controllers.bo.routes.BackofficeUserController.renderBackofficeUserIndex().toString());
				put(label, request.uri());
			}
		}, true);
	}

}
