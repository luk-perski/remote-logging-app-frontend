package controllers.remote.logging;

import models.db.remote.logging.Project;
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

    public Result add(Http.Request request, Long managerId) {
        Project project = ApiUtils.getObjectFromRequest(request, Project.class);
        projectService.add(project, managerId);
        return ApiUtils.getOkResult(project);
    }

    public Result update(Http.Request request) {
        Project project = ApiUtils.getObjectFromRequest(request, Project.class);
        projectService.update(project);
        return ApiUtils.getOkResult(project);
    }

    public Result getAll(){
        List<Project> projects = projectService.getAll();
        return ApiUtils.getOkResult(projects);
    }
}
