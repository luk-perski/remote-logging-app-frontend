package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class ColComponent extends FormDataComponent {

	public static final String COMPONENT = "col-component";

	private String grid_css_classes;

	private String extra_css_classes;

	public ColComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.grid_css_classes = element.getPropertyValue("grid_css_classes");
			this.extra_css_classes = element.getPropertyValue("extra_css_classes");
		}
	}

	@JsonProperty("grid_css_classes")
	public String getGridCSSClasses() {
		return grid_css_classes;
	}

	@JsonProperty("extra_css_classes")
	public String getExtraCSSClasses() {
		return extra_css_classes;
	}

	public void setGridCSSClasses(String grid_css_classes) {
		this.grid_css_classes = grid_css_classes;
	}

	public void setExtraCSSClasses(String extra_css_classes) {
		this.extra_css_classes = extra_css_classes;
	}

}
