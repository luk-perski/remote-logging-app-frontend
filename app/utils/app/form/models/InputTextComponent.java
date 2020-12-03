package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class InputTextComponent extends FormDataComponent {

	public static final String COMPONENT = "input-text-component";

	private String name;

	private String label_pt;

	private String label_en;

	private String placeholder_pt;

	private String placeholder_en;

	private String css_classes;

	public InputTextComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.name = element.getPropertyValue("name");
			this.label_pt = element.getPropertyValue("label_pt");
			this.label_en = element.getPropertyValue("label_en");
			this.placeholder_pt = element.getPropertyValue("placeholder_pt");
			this.placeholder_en = element.getPropertyValue("placeholder_en");
			this.css_classes = element.getPropertyValue("css_classes");
		}
	}

	public String getName() {
		return name;
	}

	@JsonProperty("label_pt")
	public String getLabelPT() {
		return label_pt;
	}

	@JsonProperty("label_en")
	public String getLabelEN() {
		return label_en;
	}

	@JsonProperty("placeholder_pt")
	public String getPlaceholderPT() {
		return placeholder_pt;
	}

	@JsonProperty("placeholder_en")
	public String getPlaceholderEN() {
		return placeholder_en;
	}

	@JsonProperty("css_classes")
	public String getCSSClasses() {
		return css_classes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLabelPT(String label_pt) {
		this.label_pt = label_pt;
	}

	public void setLabelEN(String label_en) {
		this.label_en = label_en;
	}

	public void setPlaceholderPT(String placeholder_pt) {
		this.placeholder_pt = placeholder_pt;
	}

	public void setPlaceholderEN(String placeholder_en) {
		this.placeholder_en = placeholder_en;
	}

	public void setCSSClasses(String css_classes) {
		this.css_classes = css_classes;
	}
}
