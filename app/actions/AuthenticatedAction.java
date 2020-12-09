package actions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import controllers.ajax.AJAXControllerUtils;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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
		return CompletableFuture.completedFuture(notFound());
	}
}