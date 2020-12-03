package utils.app.page;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import play.mvc.Http.RequestHeader;
import utils.Utils;

public class PageSettingsHelper {

	public PageSettings generatePageSettings(RequestHeader request, Utils u, HashMap<String, String> meta_tags, String title, boolean with_backoffice) {
		BreadcrumbElement bo_element = new BreadcrumbElement(u.l.l(request, "Backoffice"), controllers.bo.routes.BackofficeUserController.renderBackofficeIndex().toString(), false);

		BreadcrumbElement element = new BreadcrumbElement(u.l.l(request, title), request.uri(), true);

		Breadcrumbs crumbs = null;
		if (with_backoffice) {
			crumbs = u.breacrumb_helper.getBreadcrumbsWithElements(request, u.l, bo_element, element);
		} else {
			crumbs = u.breacrumb_helper.getBreadcrumbsWithElements(request, u.l, element);
		}

		return new PageSettings(request, u.l, meta_tags, crumbs);
	}

	public PageSettings generatePageSettings(RequestHeader request, Utils u, HashMap<String, String> meta_tags, LinkedHashMap<String, String> elements, boolean with_backoffice) {
		BreadcrumbElement bo_element = new BreadcrumbElement(u.l.l(request, "Backoffice"), controllers.bo.routes.BackofficeUserController.renderBackofficeIndex().toString(), false);

		Breadcrumbs crumbs = null;
		if (with_backoffice) {
			crumbs = u.breacrumb_helper.getBreadcrumbsWithElements(request, u.l, bo_element);
		} else {
			crumbs = u.breacrumb_helper.getBaseBreadcrumbs(request, u.l);
		}

		if (elements != null) {
			int size = elements.size();
			for (Entry<String, String> element : elements.entrySet()) {
				crumbs = crumbs.add(new BreadcrumbElement(u.l.l(request, element.getKey()), element.getValue(), (--size == 0)));
			}
		}

		return new PageSettings(request, u.l, meta_tags, crumbs);
	}

}
