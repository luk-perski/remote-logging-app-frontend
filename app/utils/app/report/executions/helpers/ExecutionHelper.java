package utils.app.report.executions.helpers;

import models.db.app.report.ReportExecution;
import models.db.app.report.ReportRequest;
import utils.Utils;
import utils.app.exception.ReportExecutionException;
import utils.excel.ExcelHelper;

public interface ExecutionHelper {

	public ReportExecution executeReportRequest(ReportRequest report_request, Utils utils, ExcelHelper excel_helper) throws ReportExecutionException;
}
