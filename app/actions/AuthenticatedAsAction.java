package actions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import controllers.AuthenticationController;
import controllers.ajax.AJAXControllerUtils;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.session.SessionManager;
import utils.session.exception.InvalidRoleDataException;

/**
 * Control Action to make sure user is authenticated as a certain role
 * 
 * @author alsl
 * 
 */
public class AuthenticatedAsAction extends Action<IsAuthenticatedAs> {

	private static final String AJAX_REQUEST = "XMLHttpRequest";

	@Inject
	private SessionManager session_manager;

	@Inject
	private AJAXControllerUtils ajax_controller_utils;

	public CompletionStage<Result> call(Http.Request request) {
		// Check role parameters, if authenticated as any of them, delegate to
		// appropriate url
		for (String val : configuration.value()) {
			try {
				if (this.session_manager.isAuthenticatedAs(AuthenticationController.ROLE_KEY, val, request)) {
					return delegate.call(request);
				}
			} catch (InvalidRoleDataException ignore) {
			}
		}

		// Check if the request is for an AJAX request
		String request_with = request.getHeaders().get("X-Requested-With").orElse("");
		if (request_with.equalsIgnoreCase(AJAX_REQUEST)) {
			// If so, send the corresponding error response
			return CompletableFuture.completedFuture(this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.FORBIDDEN, "error.text.forbidden"));
		}

		// If authenticated but not with one of the required roles, redirect to
		// role change page
		if (this.session_manager.isAuthenticated(request)) {
			return CompletableFuture.completedFuture(redirect(controllers.bo.routes.BackofficeUserController.renderRoleChange()));
		}

		// If not authenticated, redirect to login page
		return CompletableFuture.completedFuture(redirect(controllers.routes.AuthenticationController.renderDefaultLogin(request.uri())));
	}
}