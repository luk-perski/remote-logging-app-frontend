package controllers.remote.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.db.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Result;
import play.mvc.Results;
import service.UserService;

import java.util.List;


public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserService userService;

    public Result getAll() {
        List<User> userList = userService.getAll();
        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode result = null;
//        try {
//            result = objectMapper.readTree("{\"status\":\"ok\", \"result\":\"" + userList + "\"}");
//        } catch (JsonProcessingException e) {
//            log.error(e.getMessage());
//        }
        try {
            return Results.ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Results.badRequest();
    }
}
