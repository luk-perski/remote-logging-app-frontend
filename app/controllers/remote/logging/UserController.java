package controllers.remote.logging;

import com.google.inject.Inject;
import models.api.v1.ApiSignInData;
import models.api.v1.ApiUser;
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
        List<ApiUser> userList = userService.getAll();
        return ApiUtils.getOkResult(userList);
    }

    public Result addUserToTeam(Long userId, Long teamId) {
        userService.addUserToTeam(userId, teamId);
        return ok();
    }

    public Result getById(Long id) {
        ApiUser user = userService.getById(id);
        return ApiUtils.getOkResult(user);
    }

    public Result getByTeamId(Long teamId) {
        List<ApiUser> users = userService.getByTeamId(teamId);
        return ApiUtils.getOkResult(users);
    }

    public Result add(Http.Request request) {
        ApiUser apiUser = ApiUtils.getObjectFromRequest(request, ApiUser.class);
        apiUser = userService.add(apiUser);
        return ApiUtils.getOkResult(apiUser);
    }

    //todo make it in more save way
    public Result signIn(Http.Request request) {
        ApiSignInData signInData = ApiUtils.getObjectFromRequest(request, ApiSignInData.class);
        User user = userService.singIn(signInData);
        if (user == null) {
            return unauthorized();
        } else {
            return ApiUtils.getOkResult(user);
        }
    }
}
