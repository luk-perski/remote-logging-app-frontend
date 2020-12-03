package managers;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import models.db.log.ApplicationLog;
import models.db.sys.RedirectRecord;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Controller;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import utils.Utils;
import utils.app.page.PageSettings;
import utils.log.models.ApplicationGeneralError;
import views.html.base.basic_error_page;
import views.html.base.exception_error_page;

@Singleton
public class ErrorManager extends DefaultHttpErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorManager.class);

	private final Utils utils;

	@Inject
	public ErrorManager(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes, Utils utils) {
		super(config, environment, sourceMapper, routes);
		this.utils = utils;
	}

	@Override
	protected CompletionStage<Result> onProdServerError(RequestHeader request, UsefulException exception) {
		log.error("PROD SERVER ERROR: " + exception.getMessage());
		ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralError("PROD SERVER ERROR: " + exception.getMessage()));
		return CompletableFuture.completedFuture(Controller.internalServerError(exception_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "error.text.unexpected_error"), exception, true)));
	}

	@Override
	protected CompletionStage<Result> onForbidden(RequestHeader request, String message) {
		log.error("FORBIDDEN: " + request.path() + ((message != null && !message.trim().isEmpty()) ? ": " + message : ""));
		return CompletableFuture.completedFuture(Controller.forbidden(basic_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, "error.label.forbidden"), this.utils.l.la(request, "error.text.forbidden_resource", ((message != null && !message.trim().isEmpty()) ? message : "--")), true)));
	}

	@Override
	protected CompletionStage<Result> onBadRequest(RequestHeader request, String message) {
		log.error("BAD REQUEST: " + request.path() + " - " + ((message != null && !message.trim().isEmpty()) ? ": " + message : ""));
		return CompletableFuture.completedFuture(Controller.badRequest(basic_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, "error.label.bad_request"), this.utils.l.la(request, "error.text.bad_request", message), false)));
	}

	@Override
	protected CompletionStage<Result> onNotFound(RequestHeader request, String message) {
		// Test first if there is a redirect record for this path
		String redirect_slug = RedirectRecord.getRedirectSlugByOriginalSlug(request.path());
		if (redirect_slug != null) {
			// If so, build the redirect url and perform the redirect
			return CompletableFuture.completedFuture(Controller.redirect(generateRedirectURL(request, redirect_slug)));
		}

		log.error("NOT FOUND: " + request.method() + " " + request.path() + ((message != null && !message.trim().isEmpty()) ? ": " + message : ""));
		return CompletableFuture.completedFuture(Controller.notFound(basic_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, "error.label.page_not_found"), this.utils.l.la(request, "error.text.page_not_found", request.path()) + ((message != null && !message.trim().isEmpty()) ? ": " + message : ""), true)));
	}

	@Override
	protected CompletionStage<Result> onOtherClientError(RequestHeader request, int status_code, String message) {
		log.error("CLIENT ERROR: " + request.path() + " - " + status_code + ((message != null && !message.trim().isEmpty()) ? ": " + message : ""));
		return CompletableFuture.completedFuture(Controller.notFound(basic_error_page.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), this.utils.l.l(request, "error.label.client_error"), this.utils.l.la(request, "error.text.client_error", status_code, message), false)));
	}

	private String generateRedirectURL(RequestHeader request, String redirect_slug) {
		String query_string = "";
		Map<String, String[]> query_parameters = request.queryString();
		if (query_parameters != null) {
			for (Entry<String, String[]> query_parameter : query_parameters.entrySet()) {
				query_string += (query_string.isEmpty()) ? "?" : "&";
				query_string += query_parameter.getKey() + "=";
				if (query_parameter.getValue() != null && query_parameter.getValue().length >= 1) {
					query_string += query_parameter.getValue()[0];
				}
			}
		}
		return redirect_slug + query_string;
	}
}