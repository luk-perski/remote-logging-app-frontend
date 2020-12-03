package managers.jobs;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.db.log.ApplicationLog;
import models.db.sys.JobDescription;
import utils.log.models.ApplicationGeneralError;

public class JobsHelper {
	private static final Logger log = LoggerFactory.getLogger(JobsHelper.class);

	private static final int ONE_MINUTE_IN_MILLIS = 60000;
	private static final int NEXT_RUN_ERROR_PERIODICITY = 30;

	private static final int JOB_CHECK_EMAIL_BUFFER = 1;
	private static final int JOB_REPORT_GENERATOR = 2;

	@Inject
	private CheckEmailBufferJob check_email_buffer_job;

	@Inject
	private ReportGeneratorJob report_generator_job;

	public String runJob(JobDescription job) {
		String out = "";

		if (job != null && job.isActive() && !job.isRunning()) {
			Date next_run_save = job.getNextRun();

			try {
				job.setRunning(true);
				job.setLastRunStart(new Date());
				job.setLastRunEnd(null);
				job.setLastRunLog(null);
				job.setNextRun(null);
				job.setHasErrors(false);
				job.save();

				switch (job.getID()) {
				case JOB_CHECK_EMAIL_BUFFER:
					job.setLastRunLog(this.check_email_buffer_job.runEmailBufferCheck());
					break;
				case JOB_REPORT_GENERATOR:
					job.setLastRunLog(this.report_generator_job.runReportGenerationCheck());
					break;
				}

				job.setNextRun(calculateNextRun(next_run_save, job.getPeriodicity()));

			} catch (Exception e) {
				log.error("Error while running job: " + e.getMessage());
				ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralError("Error while running job: " + e.getMessage()));

				String last_run_log = "<p>Error while running job: " + e + "</p>";
				for (StackTraceElement ste : e.getStackTrace()) {
					last_run_log += "<p>" + ste.toString() + "</p>";
				}
				job.setLastRunLog(last_run_log);
				job.setNextRun(calculateNextRun(next_run_save, NEXT_RUN_ERROR_PERIODICITY));
				job.setHasErrors(true);
			}

			job.setRunning(false);
			job.setLastRunEnd(new Date());

			job.save();
		}

		return out;
	}

	private Date calculateNextRun(Date last_run_start, Integer periodicity) {
		if (last_run_start == null || periodicity == null) {
			return null;
		}
		Date next_run = new Date(last_run_start.getTime() + (periodicity * ONE_MINUTE_IN_MILLIS));
		// Make sure that next run is in the future
		while (next_run.compareTo(new Date()) < 0) {
			next_run = new Date(next_run.getTime() + (periodicity * ONE_MINUTE_IN_MILLIS));
		}
		return next_run;
	}
}
