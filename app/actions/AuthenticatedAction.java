package actions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import controllers.ajax.AJAXControllerUtils;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.session.SessionManager;

/**
 * Control action to make sure user is authenticated
 * 
 * @author alsl
 */
public class AuthenticatedAction extends Action.Simple {

	private static final String AJAX_REQUEST = "XMLHttpRequest";

	@Inject
	private SessionManager session_manager;

	@Inject
	private AJAXControllerUtils ajax_controller_utils;

	public CompletionStage<Result> call(Http.Request request) {
		// If authenticated, delegate to appropriate url
		if (this.session_manager.isAuthenticated(request)) {
			return delegate.call(request);
		}

		// Check if the request is for an AJAX request
		String request_with = request.getHeaders().get("X-Requested-With").orElse("");
		if (request_with.equalsIgnoreCase(AJAX_REQUEST)) {
			// If so, send the corresponding error response
			return CompletableFuture.completedFuture(this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.FORBIDDEN, "error.text.forbidden"));
		}

		// If not, redirect to login page
		return CompletableFuture.completedFuture(redirect(controllers.routes.AuthenticationController.renderDefaultLogin(request.uri())));
	}
}