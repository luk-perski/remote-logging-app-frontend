package managers.jobs;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.db.app.report.ReportExecution;
import models.db.app.report.ReportRequest;
import utils.Utils;
import utils.app.exception.ReportExecutionException;
import utils.app.report.executions.helpers.ExecutionHelper;
import utils.excel.ExcelHelper;

public class ReportGeneratorJob {

	@Inject
	private Utils utils;

	@Inject
	private ExcelHelper excel_helper;

	private static final Logger log = LoggerFactory.getLogger(ReportGeneratorJob.class);

	public String runReportGenerationCheck() throws ReportExecutionException {
		String out = "";

		log.info("Checking reports to generate...");
		out += "Checking reports to generate...\n";

		List<ReportRequest> report_requests = ReportRequest.getAllScheduled();
		if (report_requests != null && !report_requests.isEmpty()) {
			for (ReportRequest report_request : report_requests) {
				if (report_request.getReportType() != null) {
					ExecutionHelper execution_helper = report_request.getReportType().getReportExecutionClassInstance();
					if (execution_helper != null) {
						try {
							log.debug("\tExecuting report request #" + report_request.getID());
							out += "\tExecuting report request #" + report_request.getID() + "\n";

							// Set the report request in the "is executing" state
							report_request.setExecuting(true);
							report_request.save();

							// Execute the actual report
							ReportExecution report_execution = execution_helper.executeReportRequest(report_request, this.utils, this.excel_helper);
							if (report_execution != null) {
								// Get the execution log to add to the job log
								out += report_execution.getExecutionLog();

								log.debug("\tDone for #" + report_request.getID());
								out += "\tDone #" + report_request.getID() + "\n";
							} else {
								log.warn("\tReport request #" + report_request.getID() + " did not return a proper report execution");
								out += "\tReport request #" + report_request.getID() + " did not return a proper report execution\n";
							}
						} finally {
							report_request.setExecuting(false);
							report_request.save();
						}
					} else {
						log.warn("\tUnable to load execution instance for report request #" + report_request.getID());
						out += "\tUnable to load execution instance for report request #" + report_request.getID() + "\n";
					}
				} else {
					log.warn("\tReport request #" + report_request.getID() + " doesn't have an associated report type!");
					out += "\tReport request #" + report_request.getID() + " doesn't have an associated report type!\n";
				}
			}
		} else {
			log.info("\tThere are no scheduled reports.");
			out += "\tThere are no scheduled reports.\n";
		}

		log.info("Done.");
		out += "Done.\n";

		return out;
	}
}
