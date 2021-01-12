package controllers.remote.logging;

import models.api.v1.ApiLogWork;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.LogWorkService;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;


public class LogWorkController extends Controller {
    @Inject
    LogWorkService logWorkService;

    public Result add(Http.Request request) {
        ApiLogWork apiLogWork = ApiUtils.getObjectFromRequest(request, ApiLogWork.class);
        apiLogWork = logWorkService.add(apiLogWork);
        return ApiUtils.getOkResult(apiLogWork);
    }

    public Result getAll() {
        List<ApiLogWork> apiLogWorkList = logWorkService.getAll();
        return ApiUtils.getOkResult(apiLogWorkList);
    }
}
