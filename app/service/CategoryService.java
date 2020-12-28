package service;

import models.api.v1.ApiCategory;
import models.db.remote.logging.Category;
import repository.CategoryRepository;
import utils.api.v1.ModelsUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static utils.api.v1.ModelsUtils.getApiCategories;
import static utils.api.v1.ModelsUtils.getApiCategory;

public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    public ApiCategory add(String name) {
        Category category = Category.builder().name(name).cratedDate(new Date()).build();
        categoryRepository.add(category);
        return getApiCategory(category);
    }

    public List<ApiCategory> getAll() {
        List<Category> categories = categoryRepository.getAll();
        return getApiCategories(categories);
    }

    public ApiCategory getById(Long iD) {
        Category category = categoryRepository.getById(iD);
        return ModelsUtils.getApiCategory(category);
    }


}

