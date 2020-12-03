package filters;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.HandlerDef;
import play.routing.Router;

public class RequestTimeLoggingFilter extends Filter {

	private static final Logger log = LoggerFactory.getLogger(RequestTimeLoggingFilter.class);

	@Inject
	public RequestTimeLoggingFilter(Materializer materializer) {
		super(materializer);
	}

	@Override
	public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next_filter, Http.RequestHeader request_header) {
		long start_time = System.currentTimeMillis();

		return next_filter.apply(request_header).thenApply(result -> {
			String action_method = "-- No handler definition --";
			try {
				HandlerDef handlerDef = request_header.attrs().get(Router.Attrs.HANDLER_DEF);
				action_method = handlerDef.controller() + "." + handlerDef.method();
			} catch (NoSuchElementException ignore) {
			}

			long end_time = System.currentTimeMillis();
			long request_time = end_time - start_time;

			log.info("| duration: " + request_time + " ms | code: " + result.status() + " | " + request_header.method() + " " + request_header.uri() + " | [" + action_method + "]");

			return result.withHeader("Request-Time", "" + request_time + " ms");
		});
	}
}
