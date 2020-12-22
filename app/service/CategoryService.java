package service;

import models.api.v1.ApiCategory;
import models.db.remote.logging.Category;
import repository.CategoryRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    public Category add(String name) {
        Category category = Category.builder().name(name).cratedDate(new Date()).build();
        return categoryRepository.add(category);
    }

    public List<ApiCategory> getAll() {
        List<Category> categories = categoryRepository.getAll();
        return getApiCategories(categories);
    }

    public Category getById(Long iD) {
        return categoryRepository.getById(iD);
    }

    private List<ApiCategory> getApiCategories(List<Category> categories) {
        List<ApiCategory> result = new ArrayList<>();
        categories.forEach(category -> {
            result.add(ApiCategory.builder()
                    .id(category.getId())
                    .name(category.getName()).build()
            );
        });
        return result;
    }
}

