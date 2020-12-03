package models.helpers.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import models.db.app.config.ApplicationConfiguration;
import models.db.app.config.ApplicationConfigurationProperty;
import models.helpers.base.ValidationHelper;
import models.helpers.exception.FailsValidationException;
import models.helpers.exception.InvalidFormDataException;
import models.helpers.exception.InvalidModelException;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Http.Request;
import pt.iscte_iul.gdsi.utils.WebUtils;

public class ApplicationConfigurationHelper {

	// private static final Logger log = LoggerFactory.getLogger(ApplicationConfigurationHelper.class);

	@Inject
	private ValidationHelper validation_helper;

	@Inject
	private WebUtils web_utils;

	public static final String APPLICATION_NAME_PT = "application_name_pt";
	public static final String APPLICATION_NAME_EN = "application_name_en";
	public static final String APPLICATION_DESCRIPTION_PT = "application_description_pt";
	public static final String APPLICATION_DESCRIPTION_EN = "application_description_en";
	public static final String ENVIRONMENT = "environment";
	public static final String BASE_DOMAIN = "base_domain";
	public static final String BASE_URL = "base_url";
	public static final String FILES_PATH = "files_path";
	public static final String PROPERTY_LABEL = "property_label";
	public static final String PROPERTY_VALUE = "property_value";

	public void updateData(ApplicationConfiguration object, Form<?> form, Request r) throws InvalidFormDataException, FailsValidationException, InvalidModelException {
		// Perform basic checks
		if (object == null) {
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
		updateData(object, sanitized_data);
	}

	private HashMap<String, String> sanitizeData(Map<String, String> raw_data) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(APPLICATION_NAME_PT, this.web_utils.cleanHTMLNone(raw_data.get(APPLICATION_NAME_PT)));
		data.put(APPLICATION_NAME_EN, this.web_utils.cleanHTMLNone(raw_data.get(APPLICATION_NAME_EN)));
		data.put(APPLICATION_DESCRIPTION_PT, this.web_utils.cleanHTMLNone(raw_data.get(APPLICATION_DESCRIPTION_PT)));
		data.put(APPLICATION_DESCRIPTION_EN, this.web_utils.cleanHTMLNone(raw_data.get(APPLICATION_DESCRIPTION_EN)));
		data.put(ENVIRONMENT, this.web_utils.cleanHTMLNone(raw_data.get(ENVIRONMENT)));
		data.put(BASE_DOMAIN, this.web_utils.cleanHTMLNone(raw_data.get(BASE_DOMAIN)));
		data.put(BASE_URL, this.web_utils.cleanHTMLNone(raw_data.get(BASE_URL)));
		data.put(FILES_PATH, this.web_utils.cleanHTMLNone(raw_data.get(FILES_PATH)));
		return data;
	}

	private void validateData(HashMap<String, String> data, Request r) throws FailsValidationException {
		String application_name_pt = data.get(APPLICATION_NAME_PT);
		this.validation_helper.validateNotEmpty(application_name_pt, "system.label.application_name_pt", r);
		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_APPLICATION_NAME, application_name_pt, "system.label.application_name_pt", r);

		String application_name_en = data.get(APPLICATION_NAME_EN);
		this.validation_helper.validateNotEmpty(application_name_en, "system.label.application_name_en", r);
		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_APPLICATION_NAME, application_name_en, "system.label.application_name_en", r);

		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_APPLICATION_DESCRIPTION, data.get(APPLICATION_DESCRIPTION_PT), "system.label.application_description_pt", r);

		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_APPLICATION_DESCRIPTION, data.get(APPLICATION_DESCRIPTION_EN), "system.label.application_description_en", r);

		String environment = data.get(ENVIRONMENT);
		this.validation_helper.validateNotEmpty(environment, "system.label.environment", r);
		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_ENVIRONMENT, environment, "system.label.environment", r);

		String base_domain = data.get(BASE_DOMAIN);
		this.validation_helper.validateNotEmpty(base_domain, "system.label.base_domain", r);
		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_BASE_DOMAIN, base_domain, "system.label.base_domain", r);

		String base_url = data.get(BASE_URL);
		this.validation_helper.validateNotEmpty(base_url, "system.label.base_url", r);
		this.validation_helper.validateMaxLength(ApplicationConfiguration.MAX_SIZE_BASE_URL, base_url, "system.label.base_url", r);

		this.validation_helper.validateNotEmpty(data.get(FILES_PATH), "system.label.files_path", r);
	}

	private void updateData(ApplicationConfiguration object, HashMap<String, String> sanitized_data) {
		object.setApplicationNamePT(sanitized_data.get(APPLICATION_NAME_PT));
		object.setApplicationNameEN(sanitized_data.get(APPLICATION_NAME_EN));
		object.setApplicationDescriptionPT(sanitized_data.get(APPLICATION_DESCRIPTION_PT));
		object.setApplicationDescriptionEN(sanitized_data.get(APPLICATION_DESCRIPTION_EN));
		object.setEnvironment(sanitized_data.get(ENVIRONMENT));
		object.setBaseDomain(sanitized_data.get(BASE_DOMAIN));
		object.setBaseURL(sanitized_data.get(BASE_URL));
		object.setFilesPath(sanitized_data.get(FILES_PATH));
		object.save();

	}

	private HashMap<String, String> preparePropertyData(ApplicationConfiguration object, DynamicForm form, Request request) throws FailsValidationException, InvalidModelException, InvalidFormDataException {
		// Perform basic checks
		if (object == null) {
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
		HashMap<String, String> sanitized_data = sanitizePropertyData(data);

		// Validate the data
		validatePropertyData(sanitized_data, request);

		return sanitized_data;
	}

	public void addProperty(ApplicationConfiguration object, DynamicForm form, Request request) throws InvalidModelException, InvalidFormDataException, FailsValidationException {
		HashMap<String, String> sanitized_data = preparePropertyData(object, form, request);
		object.addProperty(sanitized_data.get(PROPERTY_LABEL), sanitized_data.get(PROPERTY_VALUE));
	}

	public void updateProperty(ApplicationConfiguration object, Integer property_id, DynamicForm form, Request request) throws InvalidModelException, InvalidFormDataException, FailsValidationException {
		HashMap<String, String> sanitized_data = preparePropertyData(object, form, request);
		ApplicationConfigurationProperty property = object.getPropertyByID(property_id);
		if (property != null) {
			property.updateData(sanitized_data.get(PROPERTY_LABEL), sanitized_data.get(PROPERTY_VALUE));
			property.save();
		} else {
			throw new InvalidFormDataException("Property not found");
		}
	}

	public void deleteProperty(ApplicationConfiguration object, Integer property_id, Request request) throws InvalidFormDataException {
		if (object != null) {
			ApplicationConfigurationProperty property = object.getPropertyByID(property_id);
			if (property != null) {
				object.getProperties().remove(property);
				property.delete();
			} else {
				throw new InvalidFormDataException("Property not found");
			}
		}
	}

	public void deleteProperty(ApplicationConfiguration object, Integer property_id) throws InvalidFormDataException {
		ApplicationConfigurationProperty property = ApplicationConfigurationProperty.getByID(property_id);
		if (property != null) {
			property.delete();
		}

		throw new InvalidFormDataException();
	}

	private HashMap<String, String> sanitizePropertyData(Map<String, String> raw_data) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put(PROPERTY_LABEL, this.web_utils.cleanHTMLNone(raw_data.get(PROPERTY_LABEL)));
		data.put(PROPERTY_VALUE, this.web_utils.cleanHTMLNone(raw_data.get(PROPERTY_VALUE)));
		return data;
	}

	private void validatePropertyData(HashMap<String, String> data, Request r) throws FailsValidationException {
		String property_label = data.get(PROPERTY_LABEL);
		this.validation_helper.validateNotEmpty(property_label, "system.label.property_label", r);
		this.validation_helper.validateMaxLength(ApplicationConfigurationProperty.MAX_SIZE_LABEL, property_label, "system.label.property_label", r);

		String property_value = data.get(PROPERTY_VALUE);
		this.validation_helper.validateNotEmpty(property_value, "system.label.property_value", r);
		this.validation_helper.validateMaxLength(ApplicationConfigurationProperty.MAX_SIZE_VALUE, property_value, "system.label.property_value", r);
	}
}
