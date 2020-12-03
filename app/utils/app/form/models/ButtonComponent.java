package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class ButtonComponent extends FormDataComponent {

	public static final String COMPONENT = "button-component";

	private String type;

	private String theme;

	private String extra_css_classes;

	public ButtonComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.type = element.getPropertyValue("type");
			this.theme = element.getPropertyValue("theme");
			this.extra_css_classes = element.getPropertyValue("extra_css_classes");
		}
	}

	public String getType() {
		return type;
	}

	public String getTheme() {
		return theme;
	}

	@JsonProperty("extra_css_classes")
	public String getExtraCSSClasses() {
		return extra_css_classes;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setExtraCSSClasses(String extra_css_classes) {
		this.extra_css_classes = extra_css_classes;
	}

}
