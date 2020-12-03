package utils.app.form.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class SelectComponent extends FormDataComponent {

	public static final String COMPONENT = "select-component";

	private String name;

	private String label_pt;

	private String label_en;

	private String css_classes;

	private List<SelectOption> options;

	public SelectComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.name = element.getPropertyValue("name");
			this.label_pt = element.getPropertyValue("label_pt");
			this.label_en = element.getPropertyValue("label_en");
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

	@JsonProperty("css_classes")
	public String getCSSClasses() {
		return css_classes;
	}

	public List<SelectOption> getOptions() {
		return options;
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

	public void setCSSClasses(String css_classes) {
		this.css_classes = css_classes;
	}

	public void setOptions(List<SelectOption> options) {
		this.options = options;
	}

	public void addOption(SelectOption option) {
		if (this.options == null) {
			this.options = new ArrayList<SelectOption>();
		}
		this.options.add(option);
	}
}
