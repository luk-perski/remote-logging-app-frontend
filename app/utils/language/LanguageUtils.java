package utils.language;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http.RequestHeader;
import play.twirl.api.Html;

@Singleton
public class LanguageUtils {

	private static final Logger log = LoggerFactory.getLogger(LanguageUtils.class);

	public static final String LANG_PT = "pt";
	public static final String LANG_EN = "en";

	@Inject
	private MessagesApi messages_api;

	public String l(RequestHeader request, String term) {
		return this.messages_api.preferred(request).at(term, new Object[] {});
	}

	public String la(RequestHeader request, String term, Object... objects) {
		return this.messages_api.preferred(request).at(term, objects);
	}

	public Html lh(RequestHeader request, String term) {
		return Html.apply(this.messages_api.preferred(request).at(term, new Object[] {}));
	}

	public Html lha(RequestHeader request, String term, Object... objects) {
		return Html.apply(this.messages_api.preferred(request).at(term, objects));
	}

	public String lto(RequestHeader request, Object object, String term) {
		if (object != null && term != null && !term.trim().isEmpty()) {
			Messages messages = this.messages_api.preferred(request);
			if (messages != null) {
				Lang lang = messages.lang();
				if (lang != null) {
					try {
						Method method = object.getClass().getDeclaredMethod(term + lang.code().toUpperCase());
						if (method != null) {
							return (String) method.invoke(object, new Object[] {});
						}
					} catch (NoSuchMethodException e) {
						log.warn("NoSuchMethodException: " + e.getMessage());
					} catch (SecurityException e) {
						log.warn("SecurityException: " + e.getMessage());
					} catch (IllegalArgumentException e) {
						log.warn("IllegalArgumentException: " + e.getMessage());
					} catch (IllegalAccessException e) {
						log.warn("IllegalAccessException: " + e.getMessage());
					} catch (ClassCastException e) {
						log.warn("ClassCastException: " + e.getMessage());
					} catch (InvocationTargetException e) {
						log.warn("InvocationTargetException: " + e.getMessage());
					}
				} else {
					log.warn("NULL lang!");
				}
			} else {
				log.warn("NULL messages!");
			}
		} else {
			log.warn("NULL object or term!");
		}
		return null;
	}

	public String getLanguage(RequestHeader request) {
		return this.messages_api.preferred(request).lang().code();
	}

	public String getAlternateLanguage(RequestHeader request) {
		String lang = getLanguage(request);
		if (lang != null && lang.trim().equals(LANG_EN)) {
			return LANG_PT;
		}
		return LANG_EN;
	}

	public play.api.i18n.Messages getPreferredLanguageAsScala(RequestHeader request) {
		return this.messages_api.preferred(request).asScala();
	}
}
