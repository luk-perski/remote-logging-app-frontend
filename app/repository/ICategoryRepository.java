package repository;

import models.db.remote.logging.Category;

import java.util.List;

public interface ICategoryRepository {
    List<Category> getAll();

    Category add(Category category);

    Category getById(Long id);
}
