package models.db.app.report;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;
import utils.app.report.configurations.helpers.ConfigurationHelper;
import utils.app.report.executions.helpers.ExecutionHelper;

@Entity
@Table(name = "app_report_report_type")
public class ReportType extends Model {

	private static final int MAX_SIZE_DESCRIPTION = 1000;

	@Id
	private Integer id;

	@Column(nullable = false)
	private String label_pt;

	@Column(nullable = false)
	private String label_en;

	@Column(nullable = false, length = MAX_SIZE_DESCRIPTION)
	private String description_pt;

	@Column(nullable = false, length = MAX_SIZE_DESCRIPTION)
	private String description_en;

	// This is the Java class that represents the data structure for this report type's configuration
	private String report_config_class;

	// This is the Java class that should be used to render the configuration screen for this report type and process the submission of the
	// configuration screen form
	private String report_config_helper_class;

	// This is the Java class that should be used to execute the report
	@Column(nullable = false)
	private String report_execution_class;

	// The roles that can access this report type
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "report_type")
	private List<ReportTypeRole> roles;

	// Whether this report type is currently active (that is, users can create requests for this type of report)
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
	private Boolean is_active;

	private static final Finder<Integer, ReportType> finder = new Finder<Integer, ReportType>(ReportType.class);

	public static ReportType getByID(Integer type_id) {
		return (type_id == null) ? null : finder.byId(type_id);
	}

	public static List<ReportType> getAllForRole(Integer role_id) {
		return (role_id == null) ? null : finder.query().where().eq("roles.role.id", role_id).eq("roles.has_access", true).findList();
	}

	public ReportType() {
	}

	public Integer getID() {
		return id;
	}

	public String getLabelPT() {
		return label_pt;
	}

	public String getLabelEN() {
		return label_en;
	}

	public String getDescriptionPT() {
		return description_pt;
	}

	public String getDescriptionEN() {
		return description_en;
	}

	public String getReportConfigClass() {
		return this.report_config_class;
	}

	public List<ReportTypeRole> getRoles() {
		return this.roles;
	}

	public boolean isActive() {
		return this.is_active != null && this.is_active;
	}

	public ExecutionHelper getReportExecutionClassInstance() {
		if (this.report_execution_class != null) {
			try {
				return (ExecutionHelper) Class.forName(this.report_execution_class).newInstance();
			} catch (ClassNotFoundException ignore) {
				ignore.printStackTrace();
			} catch (InstantiationException ignore) {
				ignore.printStackTrace();
			} catch (IllegalAccessException ignore) {
				ignore.printStackTrace();
			}
		}
		return null;
	}

	public ConfigurationHelper getConfigurationHelperClassInstance() {
		if (this.report_config_helper_class != null) {
			try {
				Class<?> config_helper_clazz = Class.forName(this.report_config_helper_class);
				return (ConfigurationHelper) config_helper_clazz.newInstance();
			} catch (ClassNotFoundException warning) {
				warning.printStackTrace();
			} catch (InstantiationException warning) {
				warning.printStackTrace();
			} catch (IllegalAccessException warning) {
				warning.printStackTrace();
			}
		}
		return null;
	}

	public void setLabelPT(String label_pt) {
		this.label_pt = label_pt;
	}

	public void setLabelEN(String label_en) {
		this.label_en = label_en;
	}

	public void setDescriptionPT(String description_pt) {
		this.description_pt = description_pt;
	}

	public void setDescriptionEN(String description_en) {
		this.description_en = description_en;
	}

	public void setReportConfigClass(String config_class) {
		this.report_config_class = config_class;
	}

	public void setReportConfigHelperClass(String helper_class) {
		this.report_config_helper_class = helper_class;
	}

	public void setReportExecutionClass(String execution_class) {
		this.report_execution_class = execution_class;
	}

	public void setIsActive(boolean is_active) {
		this.is_active = is_active;
	}
}
