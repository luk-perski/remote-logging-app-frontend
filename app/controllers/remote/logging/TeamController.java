package controllers.remote.logging;

import models.api.v1.ApiTeam;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.TeamService;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;

public class TeamController extends Controller {
    @Inject
    TeamService teamService;

    //todo handle null team
    public Result add(Http.Request request) {
        ApiTeam team = ApiUtils.getObjectFromRequest(request, ApiTeam.class);
        teamService.add(team);
        return ApiUtils.getOkResult(team);
    }

    public Result getAll() {
        List<ApiTeam> teamList = teamService.getAll();
        return ApiUtils.getOkResult(teamList);
    }

    public Result getById(Long id) {
        ApiTeam team = teamService.getById(id);
        return ApiUtils.getOkResult(team);
    }

    public Result getByManagerId(Long managerId) {
        List<ApiTeam> teams = teamService.getByManagerId(managerId);
        return ApiUtils.getOkResult(teams);
    }
}

