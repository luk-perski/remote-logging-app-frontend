package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class FormComponent extends FormDataComponent {

	public static final String COMPONENT = "form-component";

	private String action;

	private String method;

	private String css_classes;

	private String csrf_token_name;

	private String csrf_token_value;

	public FormComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.action = element.getPropertyValue("action");
			this.method = element.getPropertyValue("method");
			this.css_classes = element.getPropertyValue("css_classes");
		}
	}

	public String getAction() {
		return action;
	}

	public String getMethod() {
		return method;
	}

	@JsonProperty("css_classes")
	public String getCSSClasses() {
		return css_classes;
	}

	@JsonProperty("csrf_token_name")
	public String getCSRFTokenName() {
		return this.csrf_token_name;
	}

	@JsonProperty("csrf_token_value")
	public String getCSRFTokenValue() {
		return this.csrf_token_value;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setCSSClasses(String css_classes) {
		this.css_classes = css_classes;
	}

	public void setCSRFTokenName(String csrf_token_name) {
		this.csrf_token_name = csrf_token_name;
	}

	public void setCSRFTokenValue(String csrf_token_value) {
		this.csrf_token_value = csrf_token_value;
	}
}
