package service;

import models.db.remote.logging.Team;
import repository.TeamRepository;

import javax.inject.Inject;

public class TeamService {
    @Inject
    TeamRepository teamRepository;

    public Team add(Team team) {
        teamRepository.add(team);
        return team;
    }
}
