package controllers.remote.logging;

import com.google.inject.Inject;
import models.api.ApiUser;
import models.db.user.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.UserService;
import utils.api.ApiUtils;

import java.util.List;


public class UserController extends Controller {


    @Inject
    private UserService userService;

    public Result getAll() {
        List<User> userList = userService.getAll();
        return ApiUtils.getOkResult(userList);
    }

    public Result addUserToTeam(Long userId, Long teamId) {
        userService.addUserToTeam(userId, teamId);
        return ok();
    }

    public Result getById(Long id) {
        User user = userService.getById(id);
        return ApiUtils.getOkResult(user);
    }

    public Result getByTeamId(Long teamId) {
        List<User> users = userService.getByTeamId(teamId);
        return ApiUtils.getOkResult(users);
    }

    public Result add(Http.Request request) {
        ApiUser apiUser = ApiUtils.getObjectFromRequest(request, ApiUser.class);
        User user = userService.add(apiUser);
        return ApiUtils.getOkResult(user);
    }

    //todo make it in more save way
    public Result signIn(String userName, String localPwd) {
        Long userId = userService.singIn(userName, localPwd);
        if (userId == -1L) {
            return forbidden();
        } else {
            return ApiUtils.getOkResult(userId);
        }
    }
}
