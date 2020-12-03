package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectOption {

	private String value;

	private String label_pt;

	private String label_en;

	private boolean is_selected;

	public SelectOption(String value, String label_pt, String label_en, boolean is_selected) {
		this.value = value;
		this.label_pt = label_pt;
		this.label_en = label_en;
		this.is_selected = is_selected;
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
		return is_selected;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setLabelPT(String labelPT) {
		this.label_pt = labelPT;
	}

	public void setLabelEN(String labelEN) {
		this.label_en = labelEN;
	}

	public void setSelected(boolean isSelected) {
		this.is_selected = isSelected;
	}

}
