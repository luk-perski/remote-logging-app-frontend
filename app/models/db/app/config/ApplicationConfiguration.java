package models.db.app.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "app_config_application_configuration")
public class ApplicationConfiguration extends Model {

	// private static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

	public static final int MAX_SIZE_APPLICATION_NAME = 50;
	public static final int MAX_SIZE_APPLICATION_DESCRIPTION = 200;
	public static final int MAX_SIZE_ENVIRONMENT = 10;
	public static final int MAX_SIZE_BASE_DOMAIN = 100;
	public static final int MAX_SIZE_BASE_URL = 100;

	public static final String ENV_DEVELOPMENT = "dev";
	public static final String ENV_TESTS = "test";
	public static final String ENV_QUALITY = "qual";
	public static final String ENV_PRODUCTION = "prod";

	public ApplicationConfiguration() {
	}

	public static ApplicationConfiguration getConfiguration() {
		return new Finder<Integer, ApplicationConfiguration>(ApplicationConfiguration.class).query().findOne();
	}

	@Id
	private Integer id;

	@Column(length = MAX_SIZE_APPLICATION_NAME, nullable = false, unique = true)
	private String application_name_pt;

	@Column(length = MAX_SIZE_APPLICATION_NAME, nullable = false, unique = true)
	private String application_name_en;

	@Column(length = MAX_SIZE_APPLICATION_DESCRIPTION)
	private String application_description_pt;

	@Column(length = MAX_SIZE_APPLICATION_DESCRIPTION)
	private String application_description_en;

	@Column(length = MAX_SIZE_ENVIRONMENT, nullable = false, unique = true)
	private String environment;

	@Column(length = MAX_SIZE_BASE_DOMAIN, nullable = false, unique = true)
	private String base_domain;

	@Column(length = MAX_SIZE_BASE_URL, nullable = false, unique = true)
	private String base_url;

	@Column(nullable = false, unique = true)
	private String files_path;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "application_configuration")
	private List<ApplicationConfigurationProperty> properties;

	public String getApplicationNamePT() {
		return application_name_pt;
	}

	public String getApplicationNameEN() {
		return application_name_en;
	}

	public String getApplicationDescriptionPT() {
		return application_description_pt;
	}

	public String getApplicationDescriptionEN() {
		return application_description_en;
	}

	public String getBaseDomain() {
		return base_domain;
	}

	public String getBaseURL() {
		return base_url;
	}

	public String getFilesPath() {
		return files_path;
	}

	public String getEnvironment() {
		return this.environment;
	}

	public boolean isDevelopment() {
		return this.environment != null && this.environment.equalsIgnoreCase(ENV_DEVELOPMENT);
	}

	public boolean isQuality() {
		return this.environment != null && this.environment.equalsIgnoreCase(ENV_QUALITY);
	}

	public boolean isProduction() {
		return this.environment != null && this.environment.equalsIgnoreCase(ENV_PRODUCTION);
	}

	public ApplicationConfigurationProperty getPropertyByID(Integer property_id) {
		if (this.properties != null && property_id != null) {
			for (ApplicationConfigurationProperty property : properties) {
				if (property.getID().intValue() == property_id.intValue()) {
					return property;
				}
			}
		}
		return null;
	}

	public ApplicationConfigurationProperty getProperty(String property_label) {
		if (this.properties != null && property_label != null) {
			for (ApplicationConfigurationProperty property : properties) {
				if (property.getLabel() != null && property.getLabel().equals(property_label)) {
					return property;
				}
			}
		}
		return null;
	}

	public String getPropertyValue(String property_label) {
		ApplicationConfigurationProperty property = getProperty(property_label);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	public List<ApplicationConfigurationProperty> getProperties() {
		return this.properties;
	}

	public void setApplicationNamePT(String application_name_pt) {
		this.application_name_pt = application_name_pt;
	}

	public void setApplicationNameEN(String application_name_en) {
		this.application_name_en = application_name_en;
	}

	public void setApplicationDescriptionPT(String application_description_pt) {
		this.application_description_pt = application_description_pt;
	}

	public void setApplicationDescriptionEN(String application_description_en) {
		this.application_description_en = application_description_en;
	}

	public void setBaseDomain(String base_domain) {
		this.base_domain = base_domain;
	}

	public void setBaseURL(String base_url) {
		this.base_url = base_url;
	}

	public void setFilesPath(String files_path) {
		this.files_path = files_path;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public boolean isValid() {
		if (this.application_name_pt == null || this.application_name_pt.trim().isEmpty()) {
			return false;
		}
		if (this.application_name_en == null || this.application_name_en.trim().isEmpty()) {
			return false;
		}
		if (this.base_domain == null || this.base_domain.trim().isEmpty()) {
			return false;
		}
		if (this.base_url == null || this.base_url.trim().isEmpty()) {
			return false;
		}
		if (this.files_path == null || this.files_path.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	public void addProperty(String property_label, String property_value) {
		if (property_label != null) {
			// If this list of properties is null, initialise it
			if (this.properties == null) {
				this.properties = new ArrayList<ApplicationConfigurationProperty>();
			}

			// Check if there's a property with that name already
			ApplicationConfigurationProperty property = this.getProperty(property_label);
			if (property != null) {
				throw new IllegalArgumentException("Property already exists");
			}

			ApplicationConfigurationProperty new_property = new ApplicationConfigurationProperty(this, property_label, property_value);
			this.properties.add(new_property);
			this.save();
		}
	}
}
