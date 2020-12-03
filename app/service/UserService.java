package service;

import models.db.remote.logging.Team;
import models.db.user.User;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.List;


public class UserService {
    @Inject
    private UserRepository userRepository;


    public List<User> getAll() {
        return userRepository.getAll();
    }

    //todo allow user belong to more that one team
    public Boolean addUser(Team team, User user) {
        user.setTeam(team);
        userRepository.update(user);
        return user.getTeam() != null;
    }
}
