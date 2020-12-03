package utils.app.page;

import java.util.HashMap;

import controllers.Assets.Asset;
import play.mvc.Http.RequestHeader;
import utils.language.LanguageUtils;

public class PageSettings {

	private static final String DEFAULT_MAIN_CSS_URL = controllers.routes.Assets.versioned(new Asset("stylesheets/main.css")).toString();
	private static final String DEFAULT_FAVICON_URL = controllers.routes.Assets.versioned(new Asset("images/favicon.ico")).toString();

	private String lang_code;

	private String alt_lang_code;

	private String main_css_url;

	private String favicon_url;

	private HashMap<String, String> meta_tags;

	private Breadcrumbs crumbs;

	private String url;

	private String alt_url;

	public PageSettings(RequestHeader request, LanguageUtils l, HashMap<String, String> meta_tags, Breadcrumbs crumbs) {
		this.lang_code = l.getLanguage(request);
		this.alt_lang_code = l.getAlternateLanguage(request);
		this.main_css_url = DEFAULT_MAIN_CSS_URL;
		this.favicon_url = DEFAULT_FAVICON_URL;
		this.meta_tags = meta_tags;
		this.crumbs = crumbs;
		this.url = request.uri();
		this.alt_url = controllers.routes.LanguageController.setLanguage(this.alt_lang_code, request.uri()).toString();
	}

	public PageSettings(String lang_code, String alt_lang_code, HashMap<String, String> meta_tags, Breadcrumbs crumbs, String url, String alt_url) {
		this.lang_code = lang_code;
		this.alt_lang_code = alt_lang_code;
		this.main_css_url = DEFAULT_MAIN_CSS_URL;
		this.favicon_url = DEFAULT_FAVICON_URL;
		this.meta_tags = meta_tags;
		this.crumbs = crumbs;
		this.url = url;
		this.alt_url = alt_url;
	}

	public void setMainCSSURL(String url) {
		this.main_css_url = url;
	}

	public void setFaviconURL(String url) {
		this.favicon_url = url;
	}

	public String getLanguageCode() {
		return this.lang_code;
	}

	public String getAlternateLanguageCode() {
		return this.alt_lang_code;
	}

	public String getMainCSSURL() {
		return this.main_css_url;
	}

	public String getFaviconURL() {
		return this.favicon_url;
	}

	public HashMap<String, String> getMetaTags() {
		return this.meta_tags;
	}

	public Breadcrumbs getBreadcrumbs() {
		return this.crumbs;
	}

	public String getURL() {
		return this.url;
	}

	public String getAlternateURL() {
		return this.alt_url;
	}
}
