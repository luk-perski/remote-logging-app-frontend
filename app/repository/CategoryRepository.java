package repository;

import io.ebean.Finder;
import models.db.remote.logging.Category;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CategoryRepository implements ICategoryRepository {
    Finder<Long, Category> finder = new Finder<>(Category.class);

    @Override
    public List<Category> getAll() {
        return finder.all();
    }

    @Override
    public Category add(Category category) {
        category.save();
        category.update();
        return category;
    }

    @Override
    public Category getById(Long id) {
        return id != null ? finder.byId(id) : null;
    }
}
