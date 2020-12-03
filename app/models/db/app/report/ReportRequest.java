package models.db.app.report;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.Query;
import io.ebean.RawSqlBuilder;
import models.db.user.User;
import play.libs.Json;
import play.mvc.Http.Request;
import play.twirl.api.Html;
import utils.Utils;
import utils.app.report.configurations.helpers.ConfigurationHelper;

@Entity
@Table(name = "app_report_report_request")
public class ReportRequest extends Model {

	public static final String IS_RECURRENT_PROPERTY = "is_recurrent";
	public static final String PERIODICITY_PROPERTY = "periodicity";

	@Id
	private Long id;

	// The type of report that was requested
	@ManyToOne
	@Column(nullable = false)
	private ReportType report_type;

	// The user that requested the report
	@ManyToOne
	@Column(nullable = false)
	private User request_user;

	// The date in which the user requested the report
	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date request_date;

	// If the report request is recurrent
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_recurrent;

	// The periodicity of the request (only to be used if is_recurrent = true)
	private Integer periodicity;

	// JSON data that represents the configuration of this specific report request
	@Column(columnDefinition = "TEXT")
	private String configuration;

	// The executions that this report request originated
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "report_request")
	@OrderBy(value = "execution_date_end DESC")
	private List<ReportExecution> executions;

	// If this report request is currently active
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
	private Boolean is_active;

	// If this report request is currently being executed
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_executing;

	private static final Finder<Long, ReportRequest> finder = new Finder<Long, ReportRequest>(ReportRequest.class);

	public static ReportRequest getByID(Long id) {
		return (id == null) ? null : finder.byId(id);
	}

	public static List<ReportRequest> getAllByUser(Long user_id) {
		return (user_id == null) ? null : finder.query().where().eq("request_user.id", user_id).orderBy("request_date DESC").findList();
	}

	public static List<ReportRequest> getAllScheduled() {

		String sql = "";
		sql += " SELECT rr.id FROM app_report_report_request rr ";
		sql += " WHERE rr.request_date <= NOW() ";
		sql += " AND rr.is_active = true ";
		sql += " AND ( ";
		sql += " 	NOT EXISTS (SELECT 1 FROM app_report_report_execution re WHERE re.report_request_id = rr.id) ";
		sql += " 	OR ";
		sql += " 	( ";
		sql += " 		rr.is_recurrent = true ";
		sql += " 		AND ";
		sql += " 		rr.periodicity <= ( SELECT TIMESTAMPDIFF(MINUTE, MAX(re.execution_date_begin), NOW()) FROM app_report_report_execution re WHERE re.report_request_id = rr.id ) ";
		sql += " 	) ";
		sql += " ) ";

		RawSqlBuilder builder = RawSqlBuilder.parse(sql);
		Query<ReportRequest> query = Ebean.find(ReportRequest.class).setRawSql(builder.create());
		return query.findList();
	}

	public Long getID() {
		return id;
	}

	public ReportType getReportType() {
		return report_type;
	}

	public User getRequestUser() {
		return request_user;
	}

	public Date getRequestDate() {
		return request_date;
	}

	public boolean isRecurrent() {
		return this.is_recurrent != null && this.is_recurrent;
	}

	public Integer getPeriodicity() {
		return periodicity;
	}

	public String getConfiguration() {
		return configuration;
	}

	public boolean hasExecutions() {
		return this.executions != null && !this.executions.isEmpty();
	}

	public List<ReportExecution> getExecutions() {
		return executions;
	}

	public boolean isActive() {
		return this.is_active != null && this.is_active;
	}

	public boolean isExecuting() {
		return this.is_executing != null && this.is_executing;
	}

	public Object getConfigurationInstance() throws ClassNotFoundException {
		if (this.report_type != null && this.configuration != null && this.report_type.getReportConfigClass() != null) {
			Class<?> config_class = Class.forName(this.getReportType().getReportConfigClass());
			return Json.fromJson(Json.parse(this.getConfiguration()), config_class);
		}
		return null;
	}

	public Html renderConfiguration(Request request, Utils utils) {
		if (this.report_type != null && this.configuration != null) {
			ConfigurationHelper config_helper = this.report_type.getConfigurationHelperClassInstance();
			if (config_helper != null) {
				try {
					return config_helper.renderConfiguration(request, utils, this.getConfigurationInstance());
				} catch (ClassNotFoundException warning) {
					warning.printStackTrace();
				}
			}
		}
		return null;
	}

	public void setReportType(ReportType report_type) {
		this.report_type = report_type;
	}

	public void setRequestUser(User request_user) {
		this.request_user = request_user;
	}

	public void setRequestDate(Date request_date) {
		this.request_date = request_date;
	}

	public void setIsRecurrent(Boolean is_recurrent) {
		this.is_recurrent = is_recurrent;
	}

	public void setPeriodicity(Integer periodicity) {
		this.periodicity = periodicity;
	}

	public void setIsActive(boolean is_active) {
		this.is_active = is_active;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public void setExecutions(List<ReportExecution> executions) {
		this.executions = executions;
	}

	public void toggleActive() {
		this.is_active = !this.isActive();
	}

	public void setExecuting(boolean is_executing) {
		this.is_executing = is_executing;
	}
}
