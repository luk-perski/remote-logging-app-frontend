package utils.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;


public class ApiUtils {
    public static Result getOkResult(Object object) {

        final Logger log = LoggerFactory.getLogger(ApiUtils.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return badRequest(e.getMessage());
        }
    }

    public static <T> T getObjectFromRequest(Http.Request request, Class<T> className) {
        JsonNode json = request.body().asJson();
        return Json.fromJson(json, className);
    }
}
