package handlers;

import play.filters.csrf.CSRFErrorHandler;
import play.http.HttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;

//todo working only with csrf error handle
@Singleton
public class ApiHttpRequestHandler implements CSRFErrorHandler, HttpErrorHandler {

    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(badRequest(message));
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(internalServerError(exception.getMessage()));
    }

    @Override
    public CompletionStage<Result> handle(Http.RequestHeader req, String msg) {
        return CompletableFuture.completedFuture(badRequest(msg));
    }


}
