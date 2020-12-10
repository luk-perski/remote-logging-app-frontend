package controllers.remote.logging;

import models.db.remote.logging.Task;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.TaskService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;

public class TaskController extends Controller {

    @Inject
    TaskService taskService;

    public Result add(Http.Request request, Long creatorId, Long projectId, Long assigneeId, Long categoryId) {
        Task task = ApiUtils.getObjectFromRequest(request, Task.class);
        taskService.add(task, creatorId, projectId, assigneeId, categoryId);
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

    public Result getAll() {
        List<Task> tasks = taskService.getAll();
        return ApiUtils.getOkResult(tasks);
    }

    public Result getById(Long id) {
        throw new NotImplementedException();
    }
}
