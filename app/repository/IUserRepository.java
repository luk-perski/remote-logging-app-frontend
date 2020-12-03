package repository;

import models.db.user.User;

import java.util.List;

public interface IUserRepository {
    List<User> getAll();

    User add(User user);

    User update(User user);
}
