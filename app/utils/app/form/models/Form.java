package utils.app.form.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Form {

	private FormComponent form_data;

	public Form(FormComponent form_data) {
		this.form_data = form_data;
	}

	@JsonProperty("form_data")
	public FormComponent getFormData() {
		return this.form_data;
	}
}
