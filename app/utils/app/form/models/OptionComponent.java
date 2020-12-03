package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class OptionComponent extends FormDataComponent {

	public static final String COMPONENT = "option-component";

	private String value;

	private String label_pt;

	private String label_en;

	private boolean is_selected;

	private String css_classes;

	public OptionComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.value = element.getPropertyValue("value");
			this.label_pt = element.getPropertyValue("label_pt");
			this.label_en = element.getPropertyValue("label_en");
			this.is_selected = element.getBooleanPropertyValue("is_selected");
			this.css_classes = element.getPropertyValue("css_classes");
		}
	}

	public String getValue() {
		return value;
	}

	@JsonProperty("label_pt")
	public String getLabelPT() {
		return label_pt;
	}

	@JsonProperty("label_en")
	public String getLabelEN() {
		return label_en;
	}

	@JsonProperty("is_selected")
	public boolean isSelected() {
		return this.is_selected;
	}

	@JsonProperty("css_classes")
	public String getCSSClasses() {
		return css_classes;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setLabelPT(String label_pt) {
		this.label_pt = label_pt;
	}

	public void setLabelEN(String label_en) {
		this.label_en = label_en;
	}

	public void setIsSelected(boolean is_selected) {
		this.is_selected = is_selected;
	}

	public void setCSSClasses(String css_classes) {
		this.css_classes = css_classes;
	}
}
