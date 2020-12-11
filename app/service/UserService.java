package service;

import models.db.remote.logging.Team;
import models.db.user.User;
import repository.TeamRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.List;


public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private TeamRepository teamRepository;


    public List<User> getAll() {
        return userRepository.getAll();
    }

    //todo allow user belong to more that one team
    public Boolean addUserToTeam(Long userId, Long teamId) {
        User user = userRepository.getById(userId);
        Team team = teamRepository.getById(teamId);
        user.setTeam(team);
        userRepository.update(user);
        return user.getTeam() != null;
    }

    public User getById(Long id) {
        return userRepository.getById(id);
    }

    public List<User> getByTeamId(Long teamId) {
        return userRepository.getByTeamId(teamId);
    }
}
