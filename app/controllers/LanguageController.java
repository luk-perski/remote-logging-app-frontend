package controllers;

import javax.inject.Inject;

import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import pt.iscte_iul.gdsi.utils.ValidationUtils;
import utils.language.LanguageUtils;

public class LanguageController extends Controller {

	private static final String[] VALID_LANGUAGES = new String[] { LanguageUtils.LANG_PT, LanguageUtils.LANG_EN };

	@Inject
	private MessagesApi messages_api;

	@Inject
	private ValidationUtils validation_utils;

	public Result setLanguage(Request request, String language, String override_redirect) {
		if (isValidLanguage(language)) {
			Lang lang = Lang.forCode(language);
			if (lang != null) {
				// If a redirect request is present, redirect to that url
				String redirect = (override_redirect != null && !override_redirect.trim().isEmpty()) ? override_redirect : request.getHeaders().get("Referer").orElse(null);
				if (isValidURLForRedirect(redirect)) {
					return redirect(redirect).withLang(lang, this.messages_api);
				} else {
					// Otherwise, redirect to the index
					return redirect(controllers.routes.ApplicationController.index()).withLang(lang, this.messages_api);
				}
			}
		}

		// If it's not a valid language, simply redirect to referrer (if any) or to the index
		String redirect = (override_redirect != null && !override_redirect.trim().isEmpty()) ? override_redirect : request.getHeaders().get("Referer").orElse(null);
		if (isValidURLForRedirect(redirect)) {
			return redirect(redirect);
		}

		return redirect(controllers.routes.ApplicationController.index());
	}

	private boolean isValidURLForRedirect(String redirect) {
		if (redirect != null && !redirect.trim().isEmpty()) {
			return !this.validation_utils.isValidURL(redirect);
		}
		return false;
	}

	private boolean isValidLanguage(String language) {
		if (language != null && !language.trim().isEmpty()) {
			for (String valid_language : VALID_LANGUAGES) {
				if (language.trim().equalsIgnoreCase(valid_language)) {
					return true;
				}
			}
		}
		return false;
	}
}
