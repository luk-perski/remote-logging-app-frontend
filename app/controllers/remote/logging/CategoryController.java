package controllers.remote.logging;

import models.api.v1.ApiCategory;
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
        List<ApiCategory> categories = categoryService.getAll();
        return ApiUtils.getOkResult(categories);
    }

    public Result add(String categoryName) {
        ApiCategory apiCategory = categoryService.add(categoryName);
        return ApiUtils.getOkResult(apiCategory);
    }

    public Result getById(Long id) {
        ApiCategory category = categoryService.getById(id);
        return ApiUtils.getOkResult(category);
    }
}
