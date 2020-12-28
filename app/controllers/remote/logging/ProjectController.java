package controllers.remote.logging;

import models.api.v1.ApiProject;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ProjectService;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;

public class ProjectController extends Controller {

    @Inject
    ProjectService projectService;

    public Result add(Http.Request request) {
        ApiProject project = ApiUtils.getObjectFromRequest(request, ApiProject.class);
        projectService.add(project);
        return ApiUtils.getOkResult(project);
    }

    public Result update(Http.Request request) {
        ApiProject project = ApiUtils.getObjectFromRequest(request, ApiProject.class);
        projectService.update(project);
        return ApiUtils.getOkResult(project);
    }

    public Result getAll() {
        List<ApiProject> projects = projectService.getAll();
        return ApiUtils.getOkResult(projects);
    }

    public Result getById(Long id) {
        ApiProject project = projectService.getById(id);
        return ApiUtils.getOkResult(project);
    }

    public Result getByManagerId(Long managerId) {
        List<ApiProject> projects = projectService.getByManagerId(managerId);
        return ApiUtils.getOkResult(projects);
    }
}
