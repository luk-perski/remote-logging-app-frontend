package controllers;

import com.google.inject.Provider;
import jsmessages.JsMessagesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.routing.Router;
import play.routing.Router.RouteDocumentation;
import utils.Utils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests to the application's home page.
 */
public class ApplicationController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

	@Inject
	private JsMessagesFactory js_messages_factory;

	@Inject
	private Utils u;

	//Inject the current router instance
	@Inject
	Provider<Router> router;
	//Inject the current environment instance
	@Inject
	play.Environment env;

	public Result index(Request r) {
		ArrayList<String> routes = new ArrayList<>();
		if (env.isDev()) {
			//Get the list of routes
			List<RouteDocumentation> rd = router.get().documentation();
			rd.forEach(item -> {
				routes.add(item.getHttpMethod() + " " + item.getPathPattern() + " " + item.getControllerMethodInvocation());
			});
		} else {
			routes.add("Prod Mode");
		}
		return ok(views.html.index.render(routes));
	}

	/**
	 * Creates a reverse routes file to be used in javascript files
	 *
	 * @return
	 */

	/**
	 * Creates a localized version of the messages to be used in javascript files
	 * 
	 * @return
	 */
	public Result javascriptMessages(Request r) {
		return ok(js_messages_factory.all().apply(Scala.Option("window.messages"), this.u.l.getPreferredLanguageAsScala(r)));
	}
}
