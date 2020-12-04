package service;

import models.db.remote.logging.Team;
import models.db.user.User;
import repository.TeamRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class TeamService {
    @Inject
    TeamRepository teamRepository;

    @Inject
    UserRepository userRepository;

    public Team add(Team team, Long managerId) {
        User manager = userRepository.getById(managerId);
        team.setManager(manager);
        team.setCreatedDate(new Date());
        return teamRepository.add(team);
    }

    public List<Team> getAll() {
        return teamRepository.getAll();
    }
}
