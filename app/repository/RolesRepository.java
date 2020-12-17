package repository;

import io.ebean.Finder;
import models.db.user.Role;
import utils.repository.FinderUtils;

import java.util.List;

public class RolesRepository implements IRolesRepository {
    Finder<Long, Role> finder = new Finder<>(Role.class);

    @Override
    public List<Role> getAllById(List<Long> ids) {
        return FinderUtils.getObjectsByIds(finder, Role.class, ids);
    }
}
