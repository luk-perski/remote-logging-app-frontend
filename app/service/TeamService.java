package service;

import models.api.v1.ApiTeam;
import models.db.remote.logging.Team;
import repository.TeamRepository;
import repository.UserRepository;
import utils.api.v1.ModelsUtils;

import javax.inject.Inject;
import java.util.List;

import static utils.api.v1.ModelsUtils.getTeamFromApiModel;

public class TeamService {
    @Inject
    TeamRepository teamRepository;

    @Inject
    UserRepository userRepository;

    public ApiTeam add(ApiTeam apiTeam) {
        Team team = getTeamFromApiModel(apiTeam, userRepository);
        teamRepository.add(team);
        return ModelsUtils.getApiTeamFromModel(team);
    }

    public List<ApiTeam> getAll() {
        List<Team> teams = teamRepository.getAll();
        return ModelsUtils.getApiTeamListFromModels(teams);
    }

    public ApiTeam getById(Long id) {
        Team team = teamRepository.getById(id);
        return ModelsUtils.getApiTeamFromModel(team);
    }

    public List<ApiTeam> getByManagerId(Long id) {
        List<Team> teams = teamRepository.getByManagerId(id);
        return ModelsUtils.getApiTeamListFromModels(teams);
    }

}
