package service;

import models.db.remote.logging.Category;
import repository.CategoryRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    public Category add(String name) {
        Category category = Category.builder().name(name).cratedDate(new Date()).build();
        return categoryRepository.add(category);
    }

    public List<Category> getAll() {
        return categoryRepository.getAll();
    }
}
