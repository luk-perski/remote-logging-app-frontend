package controllers.remote.logging;

import models.db.remote.logging.Category;
import play.mvc.Controller;
import play.mvc.Result;
import service.CategoryService;
import utils.api.ApiUtils;

import javax.inject.Inject;
import java.util.List;

public class CategoryController extends Controller {

    @Inject
    CategoryService categoryService;

    public Result getAll() {
        List<Category> categories = categoryService.getAll();
        return ApiUtils.getOkResult(categories);
    }

    public Result add(String categoryName) {
        Category category = categoryService.add(categoryName);
        return ApiUtils.getOkResult(category);
    }

    public Result getById(Long id) {
        Category category = categoryService.getById(id);
        return ApiUtils.getOkResult(category);
    }
}
