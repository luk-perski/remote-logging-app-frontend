package service;

import models.api.ApiUser;
import models.db.remote.logging.Team;
import models.db.user.User;
import repository.TeamRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;


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
        if (!Objects.equals(userId, team.getManager().getID())) {
            user.setTeam(team);
            userRepository.update(user);
            return user.getTeam() != null;
        }
        return null; //todo handle exception
    }

    public User getById(Long id) {
        return userRepository.getById(id);
    }

    public List<User> getByTeamId(Long teamId) {
        return userRepository.getByTeamId(teamId);
    }

    public User add(ApiUser apiUser) {
        User user = User.builder().name(apiUser.getName()).username(apiUser.getUsername())
                .display_name(apiUser.getDisplayName()).email(apiUser.getEmail()).build();
        user.setPassword(apiUser.getLocalPwd());
        Team team = teamRepository.getById(apiUser.getTeamId());
        user.setTeam(team);
        return userRepository.add(user);
    }

    public Long singIn(String userName, String localPwd) {
        Long result = -1L;
        if (localPwd != null && !localPwd.isEmpty()) {
            User user = userRepository.getByUserName(userName);
            if (user.authenticate(localPwd)) {
                result = user.getID();
            }
        }
        return result;
    }
}
