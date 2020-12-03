package models.helpers.app.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import models.db.app.menu.ApplicationMenu;
import models.db.user.Role;
import models.helpers.base.ValidationHelper;
import models.helpers.exception.FailsValidationException;
import models.helpers.exception.InvalidFormDataException;
import models.helpers.exception.InvalidModelException;
import play.data.Form;
import play.mvc.Http.Request;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import pt.iscte_iul.gdsi.utils.WebUtils;

public class ApplicationMenuHelper {

	@Inject
	private ValidationHelper validation_helper;

	@Inject
	private WebUtils web_utils;

	@Inject
	private TypeUtils type_utils;

	public static final String PARENT_ID = "parent_id";
	public static final String LABEL_PT = "label_pt";
	public static final String LABEL_EN = "label_en";
	public static final String SHORT_LABEL_PT = "short_label_pt";
	public static final String SHORT_LABEL_EN = "short_label_en";
	public static final String URL = "url";
	public static final String ICON_CSS_CLASS = "icon_css_class";
	public static final String IS_ACTIVE = "is_active";
	public static final String IS_PUBLIC = "is_public";
	public static final String SHOW_WHEN_AUTHENTICATED = "show_when_authenticated";
	public static final String ORDER_WITHIN_PARENT = "order_within_parent";
	public static final String HAS_DIVIDER_BEFORE = "has_divider_before";
	public static final String OPENED_BY_DEFAULT = "opened_by_default";
	public static final String CUSTOM_VALIDATION_CLASS = "custom_validation_class";
	public static final String ROLES = "role[";

	public void createData(Form<?> form, Request r) throws InvalidFormDataException, FailsValidationException, InvalidModelException {
		updateData(new ApplicationMenu(), form, r);
	}

	public void updateData(ApplicationMenu menu, Form<?> form, Request r) throws InvalidFormDataException, FailsValidationException, InvalidModelException {
		// Perform basic checks
		if (menu == null) {
			throw new InvalidModelException();
		}
		if (form == null) {
			throw new InvalidFormDataException();
		}
		Map<String, String> data = form.rawData();
		if (data == null) {
			throw new InvalidFormDataException();
		}

		// Sanitize the data
		HashMap<String, String> sanitized_data = sanitizeData(data);

		// Validate the data
		validateData(sanitized_data, r);

		// Update the data on the object
		updateData(menu, sanitized_data);
	}

	private HashMap<String, String> sanitizeData(Map<String, String> raw_data) {
		HashMap<String, String> data = new HashMap<String, String>();

		for (Entry<String, String> entry : raw_data.entrySet()) {
			if (entry.getKey().startsWith(ROLES)) {
				data.put(entry.getKey(), this.web_utils.cleanHTMLNone(entry.getValue()));
			}
		}

		data.put(PARENT_ID, this.web_utils.cleanHTMLNone(raw_data.get(PARENT_ID)));
		data.put(LABEL_PT, this.web_utils.cleanHTMLNone(raw_data.get(LABEL_PT)));
		data.put(LABEL_EN, this.web_utils.cleanHTMLNone(raw_data.get(LABEL_EN)));
		data.put(SHORT_LABEL_PT, this.web_utils.cleanHTMLNone(raw_data.get(SHORT_LABEL_PT)));
		data.put(SHORT_LABEL_EN, this.web_utils.cleanHTMLNone(raw_data.get(SHORT_LABEL_EN)));
		data.put(URL, this.web_utils.cleanHTMLNone(raw_data.get(URL)));
		data.put(ICON_CSS_CLASS, this.web_utils.cleanHTMLNone(raw_data.get(ICON_CSS_CLASS)));
		data.put(CUSTOM_VALIDATION_CLASS, this.web_utils.cleanHTMLNone(raw_data.get(CUSTOM_VALIDATION_CLASS)));
		data.put(IS_ACTIVE, this.web_utils.cleanHTMLNone(raw_data.get(IS_ACTIVE)));
		data.put(IS_PUBLIC, this.web_utils.cleanHTMLNone(raw_data.get(IS_PUBLIC)));
		data.put(SHOW_WHEN_AUTHENTICATED, this.web_utils.cleanHTMLNone(raw_data.get(SHOW_WHEN_AUTHENTICATED)));
		data.put(HAS_DIVIDER_BEFORE, this.web_utils.cleanHTMLNone(raw_data.get(HAS_DIVIDER_BEFORE)));
		data.put(OPENED_BY_DEFAULT, this.web_utils.cleanHTMLNone(raw_data.get(OPENED_BY_DEFAULT)));
		data.put(ORDER_WITHIN_PARENT, this.web_utils.cleanHTMLNone(raw_data.get(ORDER_WITHIN_PARENT)));

		return data;
	}

	private void validateData(HashMap<String, String> data, Request r) throws FailsValidationException {

		String label_pt = data.get(LABEL_PT);
		this.validation_helper.validateNotEmpty(label_pt, "system.label.label_pt", r);
		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_LABEL, label_pt, "system.label.label_pt", r);

		String label_en = data.get(LABEL_EN);
		this.validation_helper.validateNotEmpty(label_en, "system.label.label_en", r);
		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_LABEL, label_en, "system.label.label_en", r);

		String short_label_pt = data.get(SHORT_LABEL_PT);
		this.validation_helper.validateNotEmpty(short_label_pt, "system.label.short_label_pt", r);
		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_SHORT_LABEL, short_label_pt, "system.label.short_label_pt", r);

		String short_label_en = data.get(SHORT_LABEL_EN);
		this.validation_helper.validateNotEmpty(short_label_en, "system.label.short_label_en", r);
		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_SHORT_LABEL, short_label_en, "system.label.short_label_en", r);

		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_URL, data.get(URL), "URL", r);

		this.validation_helper.validateMaxLength(ApplicationMenu.MAX_SIZE_CUSTOM_VALIDATION_CLASS, data.get(CUSTOM_VALIDATION_CLASS), "system.label.custom_validation_class", r);

		String order_within_parent = data.get(ORDER_WITHIN_PARENT);
		this.validation_helper.validateNotEmpty(order_within_parent, "system.label.order_within_parent", r);
		this.validation_helper.validatePositiveInteger(order_within_parent, "system.label.order_within_parent", r);
	}

	private void updateData(ApplicationMenu menu, HashMap<String, String> sanitized_data) {

		menu.setParent(ApplicationMenu.getByID(this.type_utils.getIntegerValue(sanitized_data.get(PARENT_ID))));

		menu.setLabelPT(sanitized_data.get(LABEL_PT));
		menu.setLabelEN(sanitized_data.get(LABEL_EN));
		menu.setShortLabelPT(sanitized_data.get(SHORT_LABEL_PT));
		menu.setShortLabelEN(sanitized_data.get(SHORT_LABEL_EN));
		menu.setURL(sanitized_data.get(URL));
		menu.setIconCSSClass(sanitized_data.get(ICON_CSS_CLASS));
		menu.setCustomValidationClass(sanitized_data.get(CUSTOM_VALIDATION_CLASS));

		String is_active = sanitized_data.get(IS_ACTIVE);
		menu.setIsActive(is_active != null && is_active.trim().equalsIgnoreCase("on"));
		String is_public = sanitized_data.get(IS_PUBLIC);
		menu.setIsPublic(is_public != null && is_public.trim().equalsIgnoreCase("on"));
		String show_when_authenticated = sanitized_data.get(SHOW_WHEN_AUTHENTICATED);
		menu.setShowWhenAuthenticated(show_when_authenticated != null && show_when_authenticated.trim().equalsIgnoreCase("on"));
		String has_divider_before = sanitized_data.get(HAS_DIVIDER_BEFORE);
		menu.setHasDividerBefore(has_divider_before != null && has_divider_before.trim().equalsIgnoreCase("on"));
		String opened_by_default = sanitized_data.get(OPENED_BY_DEFAULT);
		menu.setOpenedByDefault(opened_by_default != null && opened_by_default.trim().equalsIgnoreCase("on"));

		menu.setOrderWithinParent(this.type_utils.getIntegerValue(sanitized_data.get(ORDER_WITHIN_PARENT)));

		// Remove all role associations
		List<Role> all_roles = Role.getAll();
		if (all_roles != null) {
			for (Role role : all_roles) {
				menu.updateRoleAssociation(role, false);
			}
		}
		// Iterate parameters to update role associations that appear
		for (Entry<String, String> entry : sanitized_data.entrySet()) {
			if (entry.getKey().startsWith(ROLES)) {
				Role role = Role.getByID(this.type_utils.getIntegerValue(entry.getValue()));
				menu.updateRoleAssociation(role, true);
			}
		}

		menu.save();
	}
}
