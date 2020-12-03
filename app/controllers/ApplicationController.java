package controllers;

import controllers.Assets.Asset;
import jsmessages.JsMessagesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.routing.JavaScriptReverseRoute;
import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;
import utils.Utils;
import utils.app.page.BreadcrumbElement;
import utils.app.page.PageSettings;
import views.html.base.styles;
import views.html.index;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests to the application's home page.
 */
public class ApplicationController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

	@Inject
	private JsMessagesFactory js_messages_factory;

	@Inject
	private Utils u;

	public Result index() {
		return ok();
	}

	/**
	 * Creates a reverse routes file to be used in javascript files
	 * 
	 * @return
	 */
	public Result javascriptRoutes(Request r) {
		return ok(JavaScriptReverseRouter.create("jsRoutes", "jQuery.ajax", r.host(), getSelectedJavascriptRoutes())).as(Http.MimeTypes.JAVASCRIPT);
	}

	private JavaScriptReverseRoute[] getSelectedJavascriptRoutes() {
		return new JavaScriptReverseRoute[] {
				controllers.ajax.routes.javascript.AJAXSystemController.renderSystemJobs(),
				controllers.ajax.routes.javascript.AJAXSystemController.renderSystemJobLog(),
				controllers.ajax.routes.javascript.AJAXSystemController.getSystemLogs(),
				controllers.ajax.routes.javascript.AJAXSystemController.activateSystemJob(),
				controllers.ajax.routes.javascript.AJAXSystemController.deactivateSystemJob(),
				controllers.ajax.routes.javascript.AJAXSystemController.editSystemJob(),
				controllers.ajax.routes.javascript.AJAXSystemController.getMenuData(),
				controllers.ajax.routes.javascript.AJAXSystemController.addMenuData(),
				controllers.ajax.routes.javascript.AJAXSystemController.saveMenuData(),
				controllers.ajax.routes.javascript.AJAXSystemController.deleteMenuData(),
				controllers.ajax.routes.javascript.AJAXSystemController.getUserLogs(),
				controllers.ajax.routes.javascript.AJAXSystemController.getAllUserLogs(),
				controllers.ajax.routes.javascript.AJAXTestController.getFormData(),
				controllers.routes.javascript.MathController.ajaxSubmitCompareNumber(),
				controllers.routes.javascript.MathController.ajaxGetSumData(),
				controllers.routes.javascript.TestController.ajaxEndpoint()
		};
	}

	/**
	 * Creates a localized version of the messages to be used in javascript files
	 * 
	 * @return
	 */
	public Result javascriptMessages(Request r) {
		return ok(js_messages_factory.all().apply(Scala.Option("window.messages"), this.u.l.getPreferredLanguageAsScala(r)));
	}

	public Result renderFavicon() {
		return redirect(routes.Assets.versioned(new Asset("images/favicon.ico")));
	}

	public Result renderStylesPage(Request r) {
		return ok(styles.render(r, u, new PageSettings(r, this.u.l, null, this.u.breacrumb_helper.getBreadcrumbsWithElements(r, this.u.l, new BreadcrumbElement(u.l.l(r, "general.title.styles"), r.uri(), true)))));
	}
}
