package utils.app.report.executions.helpers.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ebean.Ebean;
import models.db.app.files.ResourceAssociatedFile;
import models.db.app.files.ResourceAssociatedFileType;
import models.db.app.report.ReportExecution;
import models.db.app.report.ReportRequest;
import models.db.user.User;
import play.libs.Json;
import utils.Utils;
import utils.app.config.AppConfig;
import utils.app.exception.ReportExecutionException;
import utils.app.file.validators.AuthenticatedUserRestrictedAccessValidator;
import utils.app.file.validators.AuthenticatedUsersValidator;
import utils.app.report.configurations.models.UserReportConfiguration;
import utils.app.report.executions.helpers.ExecutionHelper;
import utils.excel.ExcelContentProvider;
import utils.excel.ExcelHelper;
import utils.excel.exception.ExcelContentProviderException;

public class UserReportExecutionHelper implements ExecutionHelper, ExcelContentProvider {

	private static final Logger log = LoggerFactory.getLogger(UserReportExecutionHelper.class);

	private Utils utils;

	private ExcelHelper excel_helper;

	private List<User> users = null;

	private String role_label = null;

	@Override
	public ReportExecution executeReportRequest(ReportRequest report_request, Utils utils, ExcelHelper excel_helper) throws ReportExecutionException {
		this.utils = utils;
		this.excel_helper = excel_helper;

		if (report_request != null) {
			String out = "";

			Date begin_date = new Date();

			try {
				// Retrieve the report configuration
				UserReportConfiguration configuration = (UserReportConfiguration) report_request.getConfigurationInstance();
				if (configuration != null) {
					if (configuration.getFilterRole() != null) {
						this.role_label = configuration.getFilterRole().getLabelEN();

						log.debug("Filtering users with role " + this.role_label);
						out += "Filtering users with role " + this.role_label + "\n";
						this.users = Ebean.find(User.class).where().eq("roles.role.id", configuration.getFilterRole().getID()).findList();
					} else {
						log.debug("Getting entire list of users");
						out += "Getting entire list of users\n";
						this.users = Ebean.find(User.class).findList();
					}

					log.debug("Nr. users found: " + this.users.size());
					out += "Nr. users found: " + this.users.size() + "\n";

					// Generate the excel file
					String name = System.currentTimeMillis() + "_user_report" + ExcelHelper.EXCEL_EXTENSION;
					String file_base_path = "/reports/user-report/";
					String files_path = AppConfig.getInstance().getFilesPath();
					String filename = this.excel_helper.generateExcelFile(this, files_path + file_base_path, name);

					// Create the report execution
					ReportExecution report_execution = new ReportExecution();
					report_execution.setExecutionDateBegin(begin_date);
					report_execution.setExecutionDateEnd(new Date());
					report_execution.setExecutionLog(out);
					report_execution.setReportRequest(report_request);

					// Commit the report execution to the database
					report_execution.save();
					report_execution.refresh();

					// Associate excel file with the report execution
					if (filename != null) {
						File file = new File(filename);
						ResourceAssociatedFile resource_file = new ResourceAssociatedFile();
						resource_file.setResourceClass(report_execution.getClass().getCanonicalName());
						resource_file.setResourceID(report_execution.getID().toString());
						resource_file.setFileType(ResourceAssociatedFileType.getByID(ResourceAssociatedFileType.REPORT));
						resource_file.setFileName(name);
						resource_file.setFilePath(file_base_path + name);
						resource_file.setFileContentType(ExcelHelper.EXCEL_CONTENT_TYPE);
						resource_file.setFileSize(file.length());
						resource_file.setFileHash(this.utils.other_utils.getFileMD5Hash(file));
						resource_file.setIsPublic(false);
						resource_file.setRestrictedAccessValidationClass(AuthenticatedUsersValidator.class.getCanonicalName());
						if (report_request.getRequestUser() != null) {
							resource_file.setRestrictedAccessValidationData(Json.stringify(Json.toJson(new AuthenticatedUserRestrictedAccessValidator(report_request.getRequestUser().getID().longValue()))));
						}
						resource_file.setRecordAccesses(true);
						resource_file.save();
					} else {
						report_execution.setExecutionLog(report_execution.getExecutionLog() + "\nExcel generator returned an empty filename!");
						report_execution.save();
					}

					return report_execution;

				} else {
					throw new ReportExecutionException("This report has no configuration available!");
				}
			} catch (ClassNotFoundException e) {
				throw new ReportExecutionException(e);
			} catch (ClassCastException e) {
				throw new ReportExecutionException(e);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ReportExecutionException(e);
			}
		} else {
			throw new ReportExecutionException("NULL report request!");
		}
	}

	@Override
	public void addContent(Workbook workbook, ExcelHelper excel_helper) throws ExcelContentProviderException {
		try {
			// Create a new sheet
			Sheet sheet1 = workbook.createSheet();
			int sheet_index = 0;
			// Set sheet name
			workbook.setSheetName(sheet_index, "Role - " + this.role_label);

			// Create cell styles and fonts to be used in the document
			CellStyle cell_style_normal = workbook.createCellStyle();
			CellStyle cell_style_bold = workbook.createCellStyle();
			Font font_normal = workbook.createFont();
			Font font_bold = workbook.createFont();

			// Set font to 12 point type
			font_normal.setFontHeightInPoints((short) 12);
			font_bold.setFontHeightInPoints((short) 12);
			// make it color normal
			font_normal.setColor((short) Font.COLOR_NORMAL);
			font_bold.setColor((short) Font.COLOR_NORMAL);

			// make font bold
			font_normal.setBold(false);
			font_bold.setBold(true);

			// set the font
			cell_style_normal.setFont(font_normal);
			cell_style_bold.setFont(font_bold);

			// If necessary to set a specific data format, uncomment the following
			// cell_style_normal.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));

			// Declare a row object reference
			Row row = null;
			// Row index var to help manage sequence of rows
			int row_index = 0;

			// Create a row instance in sheet 1 (at index 0 - first row)
			row = sheet1.createRow(row_index++);

			int cell_index = 0;
			int cell_width = 8000;

			this.excel_helper.createCell(sheet1, row, cell_index, cell_style_bold, "ID", cell_width);
			this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_bold, "Name", cell_width);
			this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_bold, "Display Name", cell_width);
			this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_bold, "Username", cell_width);
			this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_bold, "E-mail", cell_width);

			for (User user : this.users) {
				// Create a new row instance in sheet 1
				row = sheet1.createRow(row_index++);
				// If necessary to adjust the Height of the row, uncomment the following
				// row.setHeight((short) 0x250);

				// Actually create the cell with some content
				cell_index = 0;
				this.excel_helper.createCell(sheet1, row, cell_index, cell_style_normal, user.getID(), cell_width);
				this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_normal, user.getName(), cell_width);
				this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_normal, user.getDisplayName(), cell_width);
				this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_normal, user.getUsername(), cell_width);
				this.excel_helper.createCell(sheet1, row, ++cell_index, cell_style_normal, user.getEmail(), cell_width);
			}

		} catch (Exception e) {
			throw new ExcelContentProviderException(e);
		}
	}
}
