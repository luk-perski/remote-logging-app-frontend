package repository;

import io.ebean.Finder;
import models.db.user.User;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class UserRepository implements IUserRepository {

    Finder<Integer, User> finder = new Finder<Integer, User>(User.class);

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
}
