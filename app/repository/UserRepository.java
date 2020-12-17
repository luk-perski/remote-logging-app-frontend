package repository;

import io.ebean.Finder;
import models.db.user.User;
import utils.repository.FinderUtils;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class UserRepository implements IUserRepository {

    Finder<Long, User> finder = new Finder<>(User.class);

    @Override
    public List<User> getAll() {
        return finder.all();
    }

    @Override
    public User add(User user) {
        user.save();
        user.refresh();
        return user;
    }

    @Override
    public User update(User user) {
        user.update();
        user.refresh();
        return user;
    }

    @Override
    public User getById(Long id) {
        return User.getByID(id);
    }

    @Override
    public List<User> getByTeamId(Long id) {
        return FinderUtils.getObjects(finder, User.class, id, "team.id");
    }

    @Override
    public User getByUserName(String userName) {
        try {
            return FinderUtils.getObjects(finder, User.class, userName, "username").get(0);
        } catch (Exception e) {
            //todo handle exeption
        }
        return null;
    }
}
