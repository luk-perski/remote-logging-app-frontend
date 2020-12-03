package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class RowComponent extends FormDataComponent {

	public static final String COMPONENT = "row-component";

	private String css_classes;

	public RowComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.css_classes = element.getPropertyValue("css_classes");
		}
	}

	@JsonProperty("css_classes")
	public String getCSSClasses() {
		return css_classes;
	}

	public void setCSSClasses(String css_classes) {
		this.css_classes = css_classes;
	}

}
