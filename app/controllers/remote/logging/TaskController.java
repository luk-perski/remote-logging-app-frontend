package controllers.remote.logging;

import models.api.v1.ApiTask;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.TaskService;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;

//todo add assignee to task endpoint
public class TaskController extends Controller {

    @Inject
    TaskService taskService;

    public Result add(Http.Request request) {
        ApiTask apiTask = ApiUtils.getObjectFromRequest(request, ApiTask.class);
        apiTask = taskService.add(apiTask);
        return ApiUtils.getOkResult(apiTask);
    }

    public Result update(Http.Request request) {
        ApiTask task = ApiUtils.getObjectFromRequest(request, ApiTask.class);
        taskService.update(task);
        return ApiUtils.getOkResult(task);
    }

    public Result logWork(Long taskId, Long time) {
        taskService.logWork(taskId, time);
        return ok();
    }

    public Result getAll() {
        List<ApiTask> tasks = taskService.getAll();
        return ApiUtils.getOkResult(tasks);
    }

    public Result getById(Long id) {
        ApiTask task = taskService.getById(id);
        return ApiUtils.getOkResult(task);
    }

    public Result getByAssigneeId(Long userId) {
        List<ApiTask> userTasks = taskService.getUserTasks(userId);
        return ApiUtils.getOkResult(userTasks);
    }

    public Result getByProjectId(Long projectId) {
        List<ApiTask> tasks = taskService.getByProjectId(projectId);
        return ApiUtils.getOkResult(tasks);
    }

    public Result getByCategoryId(Long categoryId) {
        List<ApiTask> tasks = taskService.getByCategoryId(categoryId);
        return ApiUtils.getOkResult(tasks);
    }

    public Result addCategoryToTask(Long taskId, Long categoryId){
        taskService.addCategoryToTask(taskId, categoryId);
        return ok();
    }

    public Result assignTaskToUser (Long taskId, Long userId){
        taskService.assignTaskToUser(taskId, userId);
        return ok();
    }
}

