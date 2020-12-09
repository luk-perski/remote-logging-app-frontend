package controllers.remote.logging;

import models.db.remote.logging.Task;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.TaskService;
import utils.api.ApiUtils;

import javax.inject.Inject;

public class TaskController extends Controller {

    @Inject
    TaskService taskService;

    public Result add(Http.Request request, Long creatorId, Long projectId) {
        Task task = ApiUtils.getObjectFromRequest(request, Task.class);
        taskService.add(task, creatorId, projectId);
        return ApiUtils.getOkResult(task);
    }

    public Result update(Http.Request request) {
        Task task = ApiUtils.getObjectFromRequest(request, Task.class);
        taskService.update(task);
        return ApiUtils.getOkResult(task);
    }

    public Result logWork(Long taskId, Long time) {
        taskService.logWork(taskId, time);
        return ok();
    }
}
