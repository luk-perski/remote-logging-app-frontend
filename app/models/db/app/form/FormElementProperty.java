package models.db.app.form;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Model;

@Entity
@Table(name = "app_form_form_element_property")
public class FormElementProperty extends Model {

	@Id
	private Long id;

	@ManyToOne
	@Column(nullable = false)
	private FormElement form_element;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String value;

	public FormElementProperty(FormElement form_element, String name, String value) {
		this.form_element = form_element;
		this.name = name;
		this.value = value;
	}

	public FormElementProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}
}
