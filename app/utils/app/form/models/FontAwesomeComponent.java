package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class FontAwesomeComponent extends FormDataComponent {

	public static final String COMPONENT = "fa-component";

	private String icon;

	private String extra_css_classes;

	public FontAwesomeComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.icon = element.getPropertyValue("icon");
			this.extra_css_classes = element.getPropertyValue("extra_css_classes");
		}
	}

	public String getIcon() {
		return icon;
	}

	@JsonProperty("extra_css_classes")
	public String getExtraCSSClasses() {
		return extra_css_classes;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setExtraCSSClasses(String extra_css_classes) {
		this.extra_css_classes = extra_css_classes;
	}

}
