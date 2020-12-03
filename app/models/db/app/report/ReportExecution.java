package models.db.app.report;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Model;
import models.db.app.files.ResourceAssociatedFile;
import pt.iscte_iul.gdsi.utils.DateUtils;

@Entity
@Table(name = "app_report_report_execution")
public class ReportExecution extends Model {

	@Id
	private Long id;

	@ManyToOne
	@Column(nullable = false)
	private ReportRequest report_request;

	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date execution_date_begin;

	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date execution_date_end;

	@Column(columnDefinition = "TEXT")
	private String execution_log;

	public Long getID() {
		return id;
	}

	public ReportRequest getReportRequest() {
		return report_request;
	}

	public Date getExecutionDateBegin() {
		return execution_date_begin;
	}

	public String getExecutionDateBeginString() {
		return new DateUtils().getStringFromDateTime(this.execution_date_begin, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	public String getExecutionDateBeginRelative() {
		return new DateUtils().getRelativeDate(this.execution_date_begin);
	}

	public Date getExecutionDateEnd() {
		return execution_date_end;
	}

	public String getExecutionDateEndString() {
		return new DateUtils().getStringFromDateTime(this.execution_date_end, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	public String getExecutionDateEndRelative() {
		return new DateUtils().getRelativeDate(this.execution_date_end);
	}

	public String getExecutionLog() {
		return execution_log;
	}

	public List<ResourceAssociatedFile> getOutputs() {
		return ResourceAssociatedFile.getByResource(ReportExecution.class.getCanonicalName(), this.id.toString());
	}

	public void setReportRequest(ReportRequest report_request) {
		this.report_request = report_request;
	}

	public void setExecutionDateBegin(Date execution_date_begin) {
		this.execution_date_begin = execution_date_begin;
	}

	public void setExecutionDateEnd(Date execution_date_end) {
		this.execution_date_end = execution_date_end;
	}

	public void setExecutionLog(String execution_log) {
		this.execution_log = execution_log;
	}
}
