package utils.app.report.configurations.helpers.impl;

import models.db.user.Role;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Http.Request;
import play.twirl.api.Html;
import utils.Utils;
import utils.app.report.configurations.helpers.ConfigurationHelper;
import utils.app.report.configurations.models.UserReportConfiguration;
import views.html.reports.partials.user_report_configuration_form_partial;

public class UserReportConfigurationHelper implements ConfigurationHelper {

	@Override
	public Html renderConfiguration(Request request, Utils utils, Object configuration) {
		if (configuration != null && configuration instanceof UserReportConfiguration) {
			UserReportConfiguration config = (UserReportConfiguration) configuration;
			if (config.getFilterRole() != null) {
				return Html.apply("<b>" + utils.l.l(request, "reports.label.chosen_role") + "</b>: " + utils.l.lto(request, config.getFilterRole(), "getLabel"));
			} else {
				return Html.apply("<p class=\"alert alert-warning\">" + utils.l.l(request, "general.text.invalid_data_sent") + "</p>");
			}
		}
		return null;
	}

	@Override
	public Html renderConfigurationForm(Request request, Utils utils) {
		return user_report_configuration_form_partial.render(request, utils, Role.getAll());
	}

	@Override
	public String processConfigurationFormSubmit(Request request, Utils utils, DynamicForm form) {
		if (form != null) {
			String filter_role_string = form.get(UserReportConfiguration.FILTER_ROLE_PROPERTY);
			Role filter_role = Role.getByID(utils.type_utils.getIntegerValue(filter_role_string));
			if (filter_role != null) {
				UserReportConfiguration configuration = new UserReportConfiguration();
				configuration.setFilterRole(filter_role);
				return Json.stringify(Json.toJson(configuration));
			}
		}
		return null;
	}
}
