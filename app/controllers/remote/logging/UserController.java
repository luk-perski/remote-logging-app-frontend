package controllers.remote.logging;

import com.google.inject.Inject;
import models.db.user.User;
import play.mvc.Controller;
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

    //todo
//    public Result addUserToTeam(){
//
//    }

    public Result getById(Long id) {
        User user = userService.getById(id);
        return ApiUtils.getOkResult(user);
    }
}
