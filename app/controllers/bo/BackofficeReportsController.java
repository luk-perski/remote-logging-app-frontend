package controllers.bo;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actions.IsAuthenticated;
import actions.IsAuthenticatedAs;
import controllers.AuthenticationController;
import io.ebean.Ebean;
import models.db.app.report.ReportExecution;
import models.db.app.report.ReportRequest;
import models.db.app.report.ReportType;
import models.db.app.report.ReportTypeRole;
import models.db.user.Role;
import models.db.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.Utils;
import utils.app.page.PageSettings;
import utils.app.report.configurations.helpers.ConfigurationHelper;
import views.html.base.basic_error_page;
import views.html.reports.report_request_create;
import views.html.reports.report_request_create_by_type;
import views.html.reports.report_request_details;
import views.html.reports.report_request_execution_access_logs;
import views.html.reports.report_request_execution_log;
import views.html.reports.report_requests;

public class BackofficeReportsController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(BackofficeReportsController.class);

	@Inject
	private Utils utils;

	@Inject
	private FormFactory ff;

	@IsAuthenticated
	public Result renderReportsIndex(Request request) {
		return redirect(controllers.bo.routes.BackofficeReportsController.renderReportRequests());
	}

	@IsAuthenticated
	public Result renderReportRequests(Request request) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeSystemController.renderSystemIndex().toString());
				put("reports.title.report_requests", request.uri());
			}
		}, true);

		return ok(report_requests.render(request, this.utils, page_settings, ReportRequest.getAllByUser(auth_user.getID())));
	}

	@IsAuthenticated
	public Result renderAddReportRequestForm(Request request) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		Role auth_role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));
		if (auth_role == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.unexpected_error"), this.utils.l.l(request, "general.text.invalid_data_sent"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeReportsController.renderReportsIndex().toString());
				put("reports.title.report_requests", controllers.bo.routes.BackofficeReportsController.renderReportRequests().toString());
				put("reports.title.report_request_create", request.uri());
			}
		}, true);

		return ok(report_request_create.render(request, this.utils, page_settings, ReportType.getAllForRole(auth_role.getID())));
	}

	@IsAuthenticated
	public Result renderAddReportRequestByTypeForm(Request request, Integer type_id) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		Role auth_role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));
		if (auth_role == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.unexpected_error"), this.utils.l.l(request, "general.text.invalid_data_sent"), false));
		}

		ReportType report_type = ReportType.getByID(type_id);
		if (report_type == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.record_not_found"), false));
		}

		boolean has_access = false;
		if (report_type.getRoles() != null) {
			for (ReportTypeRole access_role : report_type.getRoles()) {
				if (access_role.getRole() != null && access_role.getRole().getID().intValue() == auth_role.getID().intValue()) {
					has_access = access_role.hasAccess();
				}
			}
		}

		if (!has_access) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.forbidden"), this.utils.l.l(request, "error.text.forbidden"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeReportsController.renderReportsIndex().toString());
				put("reports.title.report_requests", controllers.bo.routes.BackofficeReportsController.renderReportRequests().toString());
				put("reports.title.report_request_create", request.uri());
			}
		}, true);

		return ok(report_request_create_by_type.render(request, this.utils, page_settings, report_type));
	}

	@IsAuthenticated
	public Result submitAddReportRequestByTypeForm(Request request, Integer type_id) {
		try {
			Ebean.beginTransaction();

			User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
			if (auth_user == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
			}

			Role auth_role = Role.getByID(this.utils.type_utils.getIntegerValue(this.utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY)));
			if (auth_role == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.unexpected_error"), this.utils.l.l(request, "general.text.invalid_data_sent"), false));
			}

			ReportType report_type = ReportType.getByID(type_id);
			if (report_type == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.record_not_found"), false));
			}

			boolean has_access = false;
			if (report_type.getRoles() != null) {
				for (ReportTypeRole access_role : report_type.getRoles()) {
					if (access_role.getRole() != null && access_role.getRole().getID().intValue() == auth_role.getID().intValue()) {
						has_access = access_role.hasAccess();
					}
				}
			}

			if (!has_access) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.forbidden"), this.utils.l.l(request, "error.text.forbidden"), false));
			}

			DynamicForm form = this.ff.form().bindFromRequest(request);
			if (form == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.unexpected_error"), this.utils.l.l(request, "general.text.invalid_data_sent"), false));
			}

			String config_json_data = null;
			ConfigurationHelper config_helper = report_type.getConfigurationHelperClassInstance();
			if (config_helper != null) {
				// Form processing should return the JSON data that represents the configuration for the report request
				config_json_data = config_helper.processConfigurationFormSubmit(request, utils, form);
			}

			boolean is_recurrent = form.get(ReportRequest.IS_RECURRENT_PROPERTY) != null;
			Integer periodicity = this.utils.type_utils.getIntegerValue(form.get(ReportRequest.PERIODICITY_PROPERTY));

			ReportRequest report_request = new ReportRequest();
			report_request.setConfiguration(config_json_data);
			report_request.setExecuting(false);
			report_request.setExecutions(null);
			report_request.setIsRecurrent(is_recurrent);
			report_request.setPeriodicity(periodicity);
			report_request.setReportType(report_type);
			report_request.setRequestDate(new Date());
			report_request.setRequestUser(auth_user);
			report_request.setIsActive(true);
			report_request.save();

			Ebean.commitTransaction();

			return redirect(controllers.bo.routes.BackofficeReportsController.renderReportRequests());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Ebean.endTransaction();
		}

		return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.operation_error"), false));
	}

	@IsAuthenticated
	public Result renderReportRequest(Request request, Long report_id) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeReportsController.renderReportsIndex().toString());
				put("reports.title.report_requests", controllers.bo.routes.BackofficeReportsController.renderReportRequests().toString());
				put("reports.title.report_request_details", request.uri());
			}
		}, true);

		ReportRequest report_request = ReportRequest.getByID(report_id);
		if (report_request != null) {
			if (report_request.getRequestUser() != null && auth_user.getID().longValue() == report_request.getRequestUser().getID().longValue()) {
				return ok(report_request_details.render(request, this.utils, page_settings, report_request));
			}
		}

		return ok(report_request_details.render(request, this.utils, page_settings, null));
	}

	@IsAuthenticated
	public Result renderReportExecutionLog(Request request, Long report_id, Long execution_id) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeReportsController.renderReportsIndex().toString());
				put("reports.title.report_requests", controllers.bo.routes.BackofficeReportsController.renderReportRequests().toString());
				put("reports.title.report_request_details", controllers.bo.routes.BackofficeReportsController.renderReportRequest(report_id).toString());
				put("reports.label.view_log", request.uri());
			}
		}, true);

		ReportRequest report_request = ReportRequest.getByID(report_id);
		if (report_request != null) {
			if (report_request.getRequestUser() != null && auth_user.getID().longValue() == report_request.getRequestUser().getID().longValue()) {
				for (ReportExecution execution : report_request.getExecutions()) {
					if (execution.getID().longValue() == execution_id.longValue()) {
						return ok(report_request_execution_log.render(request, this.utils, page_settings, execution));
					}
				}
			}
		}

		return ok(report_request_execution_log.render(request, this.utils, page_settings, null));
	}

	@IsAuthenticated
	public Result renderReportExecutionAccessLogs(Request request, Long report_id, Long execution_id) {
		User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
		if (auth_user == null) {
			return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
		}

		@SuppressWarnings("serial")
		PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, new LinkedHashMap<String, String>() {
			{
				put("reports.title.index", controllers.bo.routes.BackofficeReportsController.renderReportsIndex().toString());
				put("reports.title.report_requests", controllers.bo.routes.BackofficeReportsController.renderReportRequests().toString());
				put("reports.title.report_request_details", controllers.bo.routes.BackofficeReportsController.renderReportRequest(report_id).toString());
				put("reports.title.report_execution_access_logs", request.uri());
			}
		}, true);

		ReportRequest report_request = ReportRequest.getByID(report_id);
		if (report_request != null) {
			if (report_request.getRequestUser() != null && auth_user.getID().longValue() == report_request.getRequestUser().getID().longValue()) {
				for (ReportExecution execution : report_request.getExecutions()) {
					if (execution.getID().longValue() == execution_id.longValue()) {
						return ok(report_request_execution_access_logs.render(request, this.utils, page_settings, execution));
					}
				}
			}
		}

		return ok(report_request_execution_access_logs.render(request, this.utils, page_settings, null));
	}

	@IsAuthenticated
	public Result toggleReportRequestState(Request request, Long report_id) {
		try {
			Ebean.beginTransaction();

			User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));
			if (auth_user == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "general.text.invalid_data_sent"), this.utils.l.l(request, "general.text.user_not_found"), false));
			}

			ReportRequest report_request = ReportRequest.getByID(report_id);
			if (report_request != null) {
				if (report_request.getRequestUser() != null && auth_user.getID().longValue() == report_request.getRequestUser().getID().longValue()) {
					report_request.toggleActive();
					report_request.save();
				}
			}

			Ebean.commitTransaction();

			Optional<String> referer_option = request.header("referer");
			if (referer_option != null) {
				String referer = request.header("referer").get();
				if (referer != null) {
					return redirect(referer);
				}
			}

			return redirect(controllers.bo.routes.BackofficeReportsController.renderReportRequests());

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			Ebean.endTransaction();
		}

		return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.operation_error"), false));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderReportManagementIndex(Request request) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderAllReportRequests(Request request) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderReportTypes(Request request) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderAddReportTypeForm(Request request) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitAddReportTypeForm(Request request) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderEditReportTypeForm(Request request, Integer type_id) {
		// TODO
		return TODO(request);
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result submitEditReportTypeForm(Request request, Integer type_id) {
		// TODO
		return TODO(request);
	}
}
