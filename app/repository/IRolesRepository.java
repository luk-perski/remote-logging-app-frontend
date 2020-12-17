package repository;

import models.db.user.Role;

import java.util.List;

public interface IRolesRepository {
    List<Role> getAllById(List<Long> ids);
}
