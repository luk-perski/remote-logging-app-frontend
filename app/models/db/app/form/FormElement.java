package models.db.app.form;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "app_form_form_element")
public class FormElement extends Model {

	@Id
	private Long id;

	@Column(nullable = false)
	private String component;

	private String element_id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "form_element")
	private List<FormElementProperty> properties;

	@ManyToOne
	private FormElement parent;

	private Integer order_within_parent;

	@OrderBy(value = "order_within_parent ASC")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	private List<FormElement> children;

	private static final Finder<Long, FormElement> finder = new Finder<Long, FormElement>(FormElement.class);

	public static FormElement getByID(Long id) {
		return (id == null) ? null : finder.byId(id);
	}

	public FormElement() {
	}

	public Long getID() {
		return this.id;
	}

	public String getComponent() {
		return this.component;
	}

	@JsonProperty("element_id")
	public String getElementID() {
		return this.element_id;
	}

	public List<FormElementProperty> getProperties() {
		return this.properties;
	}

	public String getPropertyValue(String property_name) {
		if (this.properties != null) {
			for (FormElementProperty property : this.properties) {
				if (property.getName().equalsIgnoreCase(property_name)) {
					return property.getValue();
				}
			}
		}
		return null;
	}

	public boolean getBooleanPropertyValue(String property_name) {
		String value = getPropertyValue(property_name);
		return value != null && Boolean.parseBoolean(value);
	}

	@JsonIgnore
	public FormElement getParent() {
		return this.parent;
	}

	@JsonProperty("order_within_parent")
	public Integer getOrderWithinParent() {
		return this.order_within_parent;
	}

	public List<FormElement> getChildren() {
		return this.children;
	}

	public void setElementID(String id) {
		this.element_id = id;
	}

	public void setParent(FormElement parent) {
		this.parent = parent;
	}

	public void setOrderWithinParent(int order) {
		this.order_within_parent = order;
	}

	public void setChildren(List<FormElement> children) {
		this.children = children;
	}

	public void addChild(FormElement element) {
		if (this.children == null) {
			this.children = new ArrayList<FormElement>();
		}
		this.children.add(element);
	}
}
