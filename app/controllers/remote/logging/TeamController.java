package controllers.remote.logging;

import models.db.remote.logging.Team;
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
    public Result add(Http.Request request, Long managerId) {
        Team team = ApiUtils.getObjectFromRequest(request, Team.class);
        teamService.add(team, managerId);
        return ApiUtils.getOkResult(team);
    }

    public Result getAll() {
        List<Team> teamList = teamService.getAll();
        return ApiUtils.getOkResult(teamList);
    }
}

