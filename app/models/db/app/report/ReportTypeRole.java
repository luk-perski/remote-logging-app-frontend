package models.db.app.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ebean.Finder;
import io.ebean.Model;
import models.db.user.Role;

@Entity
@Table(name = "app_report_report_type_role")
public class ReportTypeRole extends Model {

	@Id
	private Integer id;

	@ManyToOne
	@Column(nullable = false)
	private ReportType report_type;

	@ManyToOne
	@Column(nullable = false)
	private Role role;

	// Whether or not this role has access to this report type
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean has_access;

	private static final Finder<Integer, ReportTypeRole> finder = new Finder<Integer, ReportTypeRole>(ReportTypeRole.class);

	public ReportTypeRole(ReportType report_type, Role role, boolean has_access) {
		this.report_type = report_type;
		this.role = role;
		this.has_access = has_access;
	}

	@JsonIgnore
	public ReportType getReportType() {
		return this.report_type;
	}

	public Role getRole() {
		return role;
	}

	@JsonProperty("has_access")
	public boolean hasAccess() {
		return this.has_access;
	}

	public void setHasAccess(boolean has_access) {
		this.has_access = has_access;
	}

	public static ReportTypeRole getByReporTypeForRole(Integer report_type_id, Integer role_id) {
		return finder.query().where().eq("report_type.id", report_type_id).eq("role.id", role_id).setMaxRows(1).findOne();
	}
}
