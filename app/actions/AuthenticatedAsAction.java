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
		return CompletableFuture.completedFuture(ok());
	}
}