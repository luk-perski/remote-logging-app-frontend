package models.db.sys;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ebean.Expr;
import io.ebean.Finder;
import io.ebean.Model;
import pt.iscte_iul.gdsi.utils.DateUtils;
import utils.sys.exception.SystemJobsException;

@Entity
@Table(name = "sys_job_description")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class JobDescription extends Model {

	private static final int MAX_SIZE_NAME = 200;

	@Id
	private Integer id;

	@Column(length = MAX_SIZE_NAME, nullable = false, unique = true)
	private String name_pt;

	@Column(length = MAX_SIZE_NAME, nullable = false, unique = true)
	private String name_en;

	// Periodicity of the job (in minutes)
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private Integer periodicity;

	// Last time the job started
	private Date last_run_start;

	// Last time the job ended
	private Date last_run_end;

	// The log of the last time the job executed
	@Column(columnDefinition = "MEDIUMTEXT")
	private String last_run_log;

	// The date-time for the next run of the job
	private Date next_run;

	// Whether or not the job is currently active
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_active;

	// Whether or not the job is currently running
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_running;

	// Whether or not the job has errors from the last execution
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean has_errors;

	@Version
	private Timestamp version;

	public JobDescription() {
	}

	private static Finder<Integer, JobDescription> finder = new Finder<Integer, JobDescription>(JobDescription.class);

	public static JobDescription getById(Integer job_description_id) {
		if (job_description_id == null)
			return null;
		return finder.byId(job_description_id);
	}

	public static List<JobDescription> getAll() {
		return finder.all();
	}

	public static boolean areJobsRunning() {
		return finder.query().where().eq("is_running", true).findCount() > 0;
	}

	public static List<JobDescription> getAllScheduled() {
		return finder.query().where().or(Expr.le("next_run", new Date()), Expr.isNull("next_run")).eq("is_active", true).eq("is_running", false).findList();
	}

	@JsonProperty("is_active")
	public boolean isActive() {
		return is_active != null && is_active;
	}

	@JsonProperty("is_running")
	public boolean isRunning() {
		return is_running != null && is_running;
	}

	@JsonProperty("next_run")
	public String getNextRunString() {
		return new DateUtils().getStringFromDateTime(this.next_run, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	@JsonIgnore
	public Date getNextRun() {
		return this.next_run;
	}

	@JsonProperty("next_run_relative")
	public String getNextRunRelative() {
		DateUtils du = new DateUtils();
		Date now = new Date();
		if (this.next_run != null) {
			if (now.after(this.next_run)) {
				return "< 0";
			}
		}
		return du.getRelativeTime((long) du.timeDifferenceInSeconds(now, this.next_run));
	}

	public Integer getID() {
		return this.id;
	}

	public Integer getPeriodicity() {
		return this.periodicity;
	}

	@JsonProperty("name_pt")
	public String getNamePT() {
		return this.name_pt;
	}

	@JsonProperty("name_en")
	public String getNameEN() {
		return this.name_en;
	}

	@JsonProperty("has_errors")
	public boolean hasErrors() {
		return this.has_errors != null && this.has_errors;
	}

	@JsonIgnore
	public Date getLastRunEnd() {
		return this.last_run_end;
	}

	@JsonProperty("last_run_end")
	public String getLastRunEndString() {
		return new DateUtils().getStringFromDateTime(this.last_run_end, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	@JsonProperty("last_run_end_relative")
	public String getLastRunEndRelative() {
		DateUtils du = new DateUtils();
		Date now = new Date();
		return du.getRelativeTime((long) du.timeDifferenceInSeconds(this.last_run_end, now));
	}

	@JsonIgnore
	public Date getLastRunStart() {
		return this.last_run_start;
	}

	@JsonProperty("last_run_start")
	public String getLastRunStartString() {
		return new DateUtils().getStringFromDateTime(this.last_run_start, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	@JsonProperty("last_run_start_relative")
	public String getLastRunStartRelative() {
		return new DateUtils().getRelativeDate(this.last_run_start);
	}

	@JsonProperty("last_run_execution_time")
	public String getLastRunExecutionTime() {
		DateUtils du = new DateUtils();
		return du.getRelativeTime((long) du.timeDifferenceInSeconds(this.last_run_start, this.last_run_end));
	}

	@JsonIgnore
	public String getLastRunLog() {
		return this.last_run_log;
	}

	public void setNamePT(String name_pt) {
		if (name_pt == null || name_pt.length() > MAX_SIZE_NAME) {
			throw new IllegalArgumentException("Invalid job name");
		}
		this.name_pt = name_pt;
	}

	public void setNameEN(String name_en) {
		if (name_en == null || name_en.length() > MAX_SIZE_NAME) {
			throw new IllegalArgumentException("Invalid job name");
		}
		this.name_en = name_en;
	}

	public void setPeriodicity(int periodicity) {
		this.periodicity = periodicity;
	}

	public void setNextRun(Date next_run) {
		this.next_run = next_run;
	}

	public void deactivate() throws SystemJobsException {
		if (!this.isRunning() && this.isActive()) {
			this.is_active = false;
			this.save();
		} else {
			throw new SystemJobsException("Job cannot be deactivated because it is running or it is not active");
		}
	}

	public void activate() throws SystemJobsException {
		if (!this.is_active) {
			if (this.next_run == null) {
				this.next_run = new Date();
			}
			this.is_active = true;
			this.save();
		} else {
			throw new SystemJobsException("Job cannot be activated because it is already activated");
		}
	}

	public void setRunning(boolean is_running) {
		this.is_running = is_running;
	}

	public void setLastRunStart(Date last_run_start) {
		this.last_run_start = last_run_start;
	}

	public void setLastRunEnd(Date last_run_end) {
		this.last_run_end = last_run_end;
	}

	public void setLastRunLog(String last_run_log) {
		this.last_run_log = last_run_log;
	}

	public void setHasErrors(boolean has_errors) {
		this.has_errors = has_errors;
	}
}
