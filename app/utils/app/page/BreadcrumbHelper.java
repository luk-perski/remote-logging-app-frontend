package utils.app.page;

import javax.inject.Singleton;

import play.mvc.Http.RequestHeader;
import utils.app.config.AppConfig;
import utils.language.LanguageUtils;

@Singleton
public class BreadcrumbHelper {

	private static String BASE_TITLE = null;
	private static String BASE_URL = null;

	public Breadcrumbs getBaseBreadcrumbs(RequestHeader request, LanguageUtils l) {
		if (BASE_TITLE == null) {
			BASE_TITLE = l.lto(request, AppConfig.getInstance(), "getApplicationName");
		}
		if (BASE_URL == null) {
			BASE_URL = controllers.routes.ApplicationController.index().toString();
		}
		return new Breadcrumbs().add(new BreadcrumbElement(BASE_TITLE, BASE_URL, false));
	}

	public Breadcrumbs getBreadcrumbsWithElements(RequestHeader request, LanguageUtils l, BreadcrumbElement... elements) {
		Breadcrumbs crumbs = getBaseBreadcrumbs(request, l);
		if (elements != null) {
			for (BreadcrumbElement element : elements) {
				crumbs = crumbs.add(element);
			}
		}
		return crumbs;
	}
}
