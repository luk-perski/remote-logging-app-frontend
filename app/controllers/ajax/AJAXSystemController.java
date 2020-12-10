package controllers.ajax;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;

import actions.IsAuthenticatedAs;
import io.ebean.Ebean;
import io.ebean.PagedList;
import models.db.log.ApplicationLog;
import models.db.log.UserLog;
import models.db.sys.JobDescription;
import models.db.user.Role;
import models.db.user.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import pt.iscte_iul.gdsi.utils.DateUtils;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import utils.sys.exception.SystemJobsException;

public class AJAXSystemController extends Controller {

	private static final int PAGE_SIZE = 5;

	@Inject
	private TypeUtils type_utils;

	@Inject
	private DateUtils date_utils;

	@Inject
	private AJAXControllerUtils ajax_controller_utils;

	@Inject
	private FormFactory ff;

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemJobs(Request request) {
		List<JobDescription> all_jobs = JobDescription.getAll();

		if (all_jobs != null) {
			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(all_jobs));
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newArray()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result renderSystemJobLog(Request request, Integer job_id) {
		JobDescription job = JobDescription.getById(job_id);

		if (job != null) {
			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new SystemJobLog(job.getID(), job.getLastRunLog())));
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result deactivateSystemJob(Request request, Integer job_id) {
		JobDescription job = JobDescription.getById(job_id);
		if (job != null) {
			try {
				job.deactivate();
				return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new SystemJobStatus(job.getID(), job.isActive())));
			} catch (SystemJobsException e) {
				return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result activateSystemJob(Request request, Integer job_id) {
		JobDescription job = JobDescription.getById(job_id);
		if (job != null) {
			try {
				job.activate();
				return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new SystemJobStatus(job.getID(), job.isActive())));
			} catch (SystemJobsException e) {
				return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result editSystemJob(Request request, Integer job_id) {
		JobDescription job = JobDescription.getById(job_id);
		if (job != null) {
			try {
				DynamicForm form = this.ff.form().bindFromRequest(request);
				if (form == null) {
					return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.invalid_data_sent");
				}

				Integer periodicity = this.type_utils.getIntegerValue(form.get("periodicity"));
				Date next_run = this.date_utils.getDateTimeFromString(form.get("next_run"), DateUtils.DATE_TIME_FORMAT_EXT);

				if (periodicity == null || next_run == null) {
					return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.invalid_data_sent");
				}

				job.setPeriodicity(periodicity);
				job.setNextRun(next_run);
				job.save();

				return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new SystemJobChanges(job.getID(), job.getPeriodicity(), job.getNextRunString())));
			} catch (Exception e) {
				return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
		return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.operation_error");
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result getSystemLogs(Request request, Integer nr_pages) {

		PagedList<ApplicationLog> original_logs = ApplicationLog.getAllPagedList(PAGE_SIZE * nr_pages, 0);
		if (original_logs != null && original_logs.getList() != null) {
			List<SystemLogData> system_logs = new ArrayList<SystemLogData>();

			for (ApplicationLog system_log : original_logs.getList()) {
				system_logs.add(new SystemLogData(system_log.getInstant(), system_log.getTypeLabel(), system_log.getContext(), system_log.getDescription()));
			}

			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new SystemLogsData(system_logs, original_logs.hasNext())));
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result getMenuData(Request request) {
		try {
			Ebean.beginTransaction();

			Ebean.commitTransaction();
			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.operation_error");

		} catch (Exception e) {
			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result addMenuData(Request request) {
		try {
			Ebean.beginTransaction();

			DynamicForm form = this.ff.form().bindFromRequest(request);
			if (form == null) {
				return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.invalid_data_sent");
			}

			Ebean.commitTransaction();

			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));

		} catch (Exception e) {
			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result saveMenuData(Request request, Integer menu_id) {
		try {
			Ebean.beginTransaction();

			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.operation_error");

		} catch (Exception e) {
			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result deleteMenuData(Request request, Integer menu_id) {
		try {
			Ebean.beginTransaction();

			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, "general.text.operation_error");

		} catch (Exception e) {
			return this.ajax_controller_utils.rendeAJAXErrorResponse(request, Http.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			Ebean.endTransaction();
		}
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result getAllUserLogs(Request request, Integer nr_pages) {

		PagedList<UserLog> original_logs = UserLog.getAllPagedList(PAGE_SIZE * nr_pages, 0);
		if (original_logs != null && original_logs.getList() != null) {
			List<UserLogData> user_logs = new ArrayList<UserLogData>();

			for (UserLog user_log : original_logs.getList()) {
				user_logs.add(new UserLogData(user_log.getUser(), user_log.getInstant(), user_log.getTypeLabel(), user_log.getDescription()));
			}

			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new UserLogsData(user_logs, original_logs.hasNext())));
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result getUserLogs(Request request, Long user_id, Integer nr_pages) {

		User user = User.getByID(user_id);
		if (user != null) {
			PagedList<UserLog> original_user_logs = UserLog.getByUserPagedList(user_id.toString(), PAGE_SIZE * nr_pages, 0);
			if (original_user_logs != null && original_user_logs.getList() != null) {
				List<UserLogData> user_logs = new ArrayList<UserLogData>();

				for (UserLog user_log : original_user_logs.getList()) {
					user_logs.add(new UserLogData(user, user_log.getInstant(), user_log.getTypeLabel(), user_log.getDescription()));
				}

				return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new UserLogsData(user_logs, original_user_logs.hasNext())));
			}
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(Json.newObject()));
	}
}

class SystemJobLog {

	private int job_id;

	private String job_log;

	public SystemJobLog(int job_id, String job_log) {
		this.job_id = job_id;
		this.job_log = job_log;
	}

	@JsonProperty("job_id")
	public int getJobID() {
		return this.job_id;
	}

	@JsonProperty("job_log")
	public String getJobLog() {
		return this.job_log;
	}
}

class SystemJobStatus {

	private int job_id;

	private boolean job_is_active;

	public SystemJobStatus(int job_id, boolean job_is_active) {
		this.job_id = job_id;
		this.job_is_active = job_is_active;
	}

	@JsonProperty("job_id")
	public int getJobID() {
		return this.job_id;
	}

	@JsonProperty("job_is_active")
	public boolean jobIsActive() {
		return this.job_is_active;
	}
}

class SystemJobChanges {

	private int job_id;

	private int job_periodicity;

	private String job_next_run;

	public SystemJobChanges(int job_id, int job_periodicity, String job_next_run) {
		this.job_id = job_id;
		this.job_periodicity = job_periodicity;
		this.job_next_run = job_next_run;
	}

	@JsonProperty("job_id")
	public int getJobID() {
		return this.job_id;
	}

	@JsonProperty("job_periodicity")
	public int getJobPeriodicity() {
		return this.job_periodicity;
	}

	@JsonProperty("job_next_run")
	public String getJobNextRun() {
		return this.job_next_run;
	}
}

class UserLogsData {

	private List<UserLogData> user_logs;

	private boolean has_more_data;

	public UserLogsData(List<UserLogData> user_logs, boolean has_more_data) {
		this.user_logs = user_logs;
		this.has_more_data = has_more_data;
	}

	@JsonProperty("user_logs")
	public List<UserLogData> getUserLogs() {
		return this.user_logs;
	}

	@JsonProperty("has_more_data")
	public boolean hasMoreData() {
		return this.has_more_data;
	}
}

class UserLogData {

	private User user;

	private Date log_instant;

	private String log_type;

	private String log_description;

	public UserLogData(User user, Date log_instant, String log_type, String log_description) {
		this.user = user;
		this.log_instant = log_instant;
		this.log_type = log_type;
		this.log_description = log_description;
	}

	@JsonProperty("user_name")
	public String getUserName() {
		if (user != null) {
			return user.getDisplayName();
		}
		return "--";
	}

	@JsonProperty("user_username")
	public String getUserUsername() {
		if (user != null) {
			return user.getUsername();
		}
		return "--";
	}

	@JsonProperty("user_email")
	public String getUserEmail() {
		if (user != null) {
			return user.getEmail();
		}
		return "--";
	}

	@JsonProperty("log_instant")
	public String getLogInstant() {
		return new DateUtils().getStringFromDateTime(log_instant, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	@JsonProperty("log_instant_relative")
	public String getLogInstantRelative() {
		DateUtils du = new DateUtils();
		return du.getRelativeTime((long) du.timeDifferenceInSeconds(this.log_instant, new Date()));
	}

	@JsonProperty("log_type")
	public String getLogType() {
		return log_type;
	}

	@JsonProperty("log_description")
	public String getLogDescription() {
		return log_description;
	}
}

class SystemLogsData {

	private List<SystemLogData> system_logs;

	private boolean has_more_data;

	public SystemLogsData(List<SystemLogData> system_logs, boolean has_more_data) {
		this.system_logs = system_logs;
		this.has_more_data = has_more_data;
	}

	@JsonProperty("system_logs")
	public List<SystemLogData> getSystemLogs() {
		return this.system_logs;
	}

	@JsonProperty("has_more_data")
	public boolean hasMoreData() {
		return this.has_more_data;
	}
}

class SystemLogData {

	private Date log_instant;

	private String log_type;

	private String log_context;

	private String log_description;

	public SystemLogData(Date log_instant, String log_type, String log_context, String log_description) {
		this.log_instant = log_instant;
		this.log_type = log_type;
		this.log_context = log_context;
		this.log_description = log_description;
	}

	@JsonProperty("log_instant")
	public String getLogInstant() {
		return new DateUtils().getStringFromDateTime(log_instant, DateUtils.DATE_TIME_FORMAT_EXT);
	}

	@JsonProperty("log_instant_relative")
	public String getLogInstantRelative() {
		DateUtils du = new DateUtils();
		return du.getRelativeTime((long) du.timeDifferenceInSeconds(this.log_instant, new Date()));
	}

	@JsonProperty("log_type")
	public String getLogType() {
		return log_type;
	}

	@JsonProperty("log_context")
	public String getLogContext() {
		return log_context;
	}

	@JsonProperty("log_description")
	public String getLogDescription() {
		return log_description;
	}
}