package controllers;

import java.util.List;

import javax.inject.Inject;

import io.ebean.PagedList;
import models.db.search.SearchContentResult;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.Utils;
import utils.app.page.PageSettings;
import utils.app.page.Pagination;
import utils.search.SearchHelper;
import views.html.base.basic_error_message;
import views.html.base.basic_warning_message;
import views.html.search.search_form;
import views.html.search.search_results;

public class SearchController extends Controller {

	private static final String SEARCH_TERM_KEY = "search_term";
	private static final String PAGE_KEY = "page";
	private static final int PAGE_SIZE = 10;

	@Inject
	private Utils utils;

	@Inject
	private FormFactory ff;

	@Inject
	private SearchHelper search_helper;

	public Result renderSearchForm(Request request, String search_term) {
		return performSearch(request, this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, "search.title.search_form", false), search_term, 1);
	}

	public Result submitSearchForm(Request request) {
		DynamicForm form = this.ff.form().bindFromRequest(request);
		if (form == null) {
			return performSearch(request, this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, "search.title.search_form", false), null, null);
		}

		return performSearch(request, this.utils.page_settings_helper.generatePageSettings(request, this.utils, null, "search.title.search_form", false), form.get(SEARCH_TERM_KEY), this.utils.type_utils.getIntegerValue(form.get(PAGE_KEY)));
	}

	private Result performSearch(Request request, PageSettings page_settings, String search_term, Integer page) {
		if (search_term == null || search_term.trim().isEmpty()) {
			return badRequest(search_form.render(request, utils, page_settings, search_term, basic_error_message.render(this.utils.l.l(request, "search.text.search_term_cannot_be_empty"), false)));
		}

		if (page == null) {
			page = 1;
		}

		PagedList<SearchContentResult> results = this.search_helper.performGeneralSearch(search_term, page.intValue(), PAGE_SIZE);
		if (results != null) {
			results.loadCount();
			List<SearchContentResult> list = results.getList();
			if (results.getTotalCount() > 0) {
				return ok(search_form.render(request, this.utils, page_settings, search_term, search_results.render(request, this.utils, search_term, new Pagination(results), list)));
			}
		}

		return ok(search_form.render(request, this.utils, page_settings, search_term, basic_warning_message.render(this.utils.l.l(request, "search.text.no_results_found"), false)));
	}
}
