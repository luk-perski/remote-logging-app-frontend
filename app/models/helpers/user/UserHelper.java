package models.helpers.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import models.db.user.Role;
import models.db.user.User;
import models.helpers.base.ValidationHelper;
import models.helpers.exception.FailsValidationException;
import models.helpers.exception.InvalidFormDataException;
import models.helpers.exception.InvalidModelException;
import play.data.Form;
import play.mvc.Http.Request;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import pt.iscte_iul.gdsi.utils.WebUtils;

public class UserHelper {

	@Inject
	private ValidationHelper validation_helper;

	@Inject
	private WebUtils web_utils;

	@Inject
	private TypeUtils type_utils;

	public static final String NAME = "name";
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String ROLES = "roles";

	public void updateData(User user, Form<?> form, Request r) throws InvalidFormDataException, FailsValidationException, InvalidModelException {
		// Perform basic checks
		if (user == null) {
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
		updateData(user, sanitized_data);
	}

	private HashMap<String, String> sanitizeData(Map<String, String> raw_data) {
		HashMap<String, String> data = new HashMap<String, String>();

		for (Entry<String, String> entry : raw_data.entrySet()) {
			if (entry.getKey().startsWith(ROLES)) {
				data.put(entry.getKey(), this.web_utils.cleanHTMLNone(entry.getValue()));
			}
		}

		data.put(NAME, this.web_utils.cleanHTMLNone(raw_data.get(NAME)));
		data.put(USERNAME, this.web_utils.cleanHTMLNone(raw_data.get(USERNAME)));
		data.put(EMAIL, this.web_utils.cleanHTMLNone(raw_data.get(EMAIL)));

		return data;
	}

	private void validateData(HashMap<String, String> data, Request r) throws FailsValidationException {
		String name = data.get(NAME);
		this.validation_helper.validateNotEmpty(name, "user.label.name", r);
		this.validation_helper.validateMaxLength(User.MAX_SIZE_NAME, name, "user.label.name", r);

		String username = data.get(USERNAME);
		this.validation_helper.validateNotEmpty(username, "user.label.username", r);
		this.validation_helper.validateMaxLength(User.MAX_SIZE_USERNAME, username, "user.label.username", r);

		String email = data.get(EMAIL);
		this.validation_helper.validateNotEmpty(email, "E-Mail", r);
		this.validation_helper.validateMaxLength(User.MAX_SIZE_EMAIL, email, "E-Mail", r);
		this.validation_helper.validateEmail(email, "E-Mail", r);
	}

	private void updateData(User user, HashMap<String, String> sanitized_data) {
		user.setName(sanitized_data.get(NAME));
		user.setUsername(sanitized_data.get(USERNAME));
		user.setEmail(sanitized_data.get(EMAIL));

		// Remove all role associations
		List<Role> all_roles = Role.getAll();
		if (all_roles != null) {
			for (Role role : all_roles) {
				user.updateRoleAssociation(role, false);
			}
		}
		// Iterate parameters to update role associations that appear
		for (Entry<String, String> entry : sanitized_data.entrySet()) {
			if (entry.getKey().startsWith(ROLES)) {
				Role role = Role.getByID(this.type_utils.getIntegerValue(entry.getValue()));
				user.updateRoleAssociation(role, true);
			}
		}

		user.save();
	}

}
