package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.app.form.FormElement;

public class TextComponent extends FormDataComponent {

	public static final String COMPONENT = "text-component";

	private String text_pt;

	private String text_en;

	public TextComponent(FormElement element) {
		super(element);
		if (element.getProperties() != null) {
			this.text_pt = element.getPropertyValue("text_pt");
			this.text_en = element.getPropertyValue("text_en");
		}
	}

	@JsonProperty("text_pt")
	public String getTextPT() {
		return text_pt;
	}

	@JsonProperty("text_en")
	public String getTextEN() {
		return text_en;
	}

	public void setTextPT(String text_pt) {
		this.text_pt = text_pt;
	}

	public void setTextEN(String text_en) {
		this.text_en = text_en;
	}
}
