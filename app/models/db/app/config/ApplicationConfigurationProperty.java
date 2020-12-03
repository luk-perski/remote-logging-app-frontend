package models.db.app.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "app_config_application_configuration_property")
public class ApplicationConfigurationProperty extends Model {

	public static final int MAX_SIZE_LABEL = 100;
	public static final int MAX_SIZE_VALUE = 500;

	@Id
	private Integer id;

	@ManyToOne
	private ApplicationConfiguration application_configuration;

	@Column(nullable = false, length = MAX_SIZE_LABEL)
	private String label;

	@Column(nullable = false, length = MAX_SIZE_VALUE)
	private String value;

	private static final Finder<Integer, ApplicationConfigurationProperty> finder = new Finder<Integer, ApplicationConfigurationProperty>(ApplicationConfigurationProperty.class);

	public static ApplicationConfigurationProperty getByID(Integer property_id) {
		return (property_id == null) ? null : finder.byId(property_id);
	}

	public ApplicationConfigurationProperty(ApplicationConfiguration application_configuration, String property_label, String property_value) {
		this.application_configuration = application_configuration;
		this.label = property_label;
		this.value = property_value;
	}

	public Integer getID() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	public String getValue() {
		return this.value;
	}

	public void updateData(String property_label, String property_value) {
		this.label = property_label;
		this.value = property_value;
	}
}
