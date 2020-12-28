package service;

import models.api.v1.ApiUser;
import models.db.remote.logging.Team;
import models.db.user.User;
import repository.TeamRepository;
import repository.UserRepository;
import utils.api.v1.ModelsUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;


public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private TeamRepository teamRepository;




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

    public ApiUser getById(Long id) {
        User user = userRepository.getById(id);
        return ModelsUtils.getApiUserFromModel(user);
    }

    public List<ApiUser> getByTeamId(Long teamId) {
        List<User> users = userRepository.getByTeamId(teamId);
        return ModelsUtils.getApiUserListFromModels(users);
    }

    public ApiUser add(ApiUser apiUser) {
        User user = ModelsUtils.getUserModelFromApiUser(apiUser, teamRepository);
        user.setPassword(apiUser.getLocalPwd());
        Team team = teamRepository.getById(apiUser.getTeamId());
        user.setTeam(team);
        userRepository.add(user);
        return ModelsUtils.getApiUserFromModel(user);
    }

    public List<ApiUser> getAll() {
        List<User> users = userRepository.getAll();
        return ModelsUtils.getApiUserListFromModels(users);
    }

    public User singIn(String userName, String localPwd) {
        User result = null;
        if (localPwd != null && !localPwd.isEmpty()) {
            User user = userRepository.getByUserName(userName);
            if (user != null && user.authenticate(localPwd)) {
                result = user;
            }
        }
        return result;
    }
}
