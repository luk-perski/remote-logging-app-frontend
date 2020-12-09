package utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import pt.iscte_iul.gdsi.utils.DateUtils;
import pt.iscte_iul.gdsi.utils.JavaUtils;
import pt.iscte_iul.gdsi.utils.StringUtils;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import pt.iscte_iul.gdsi.utils.ValidationUtils;
import pt.iscte_iul.gdsi.utils.WebUtils;
import utils.app.menu.MenuHelper;
import utils.language.LanguageUtils;
import utils.other.OtherUtils;
import utils.session.SessionManager;
import utils.sys.StaticContentUtils;

@Singleton
public class Utils {

	@Inject
	public SessionManager session_manager;

	@Inject
	public LanguageUtils l;

	@Inject
	public DateUtils date_utils;

	@Inject
	public JavaUtils java_utils;

	@Inject
	public StringUtils string_utils;

	@Inject
	public TypeUtils type_utils;

	@Inject
	public WebUtils web_utils;

	@Inject
	public ValidationUtils validation_utils;

	@Inject
	public OtherUtils other_utils;

	@Inject
	public MenuHelper menu_helper;

	@Inject
	public StaticContentUtils static_content_utils;
}
