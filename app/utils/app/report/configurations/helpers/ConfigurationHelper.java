package utils.app.report.configurations.helpers;

import play.data.DynamicForm;
import play.mvc.Http.Request;
import play.twirl.api.Html;
import utils.Utils;

public interface ConfigurationHelper {

	public Html renderConfiguration(Request request, Utils utils, Object configuration);

	public Html renderConfigurationForm(Request request, Utils utils);

	public String processConfigurationFormSubmit(Request request, Utils utils, DynamicForm form);
}
