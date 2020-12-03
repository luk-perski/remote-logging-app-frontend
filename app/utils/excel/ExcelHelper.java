package utils.excel;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.excel.exception.ExcelContentProviderException;

public class ExcelHelper {

	private static final String TEMP_DIRECTORY = "temp" + File.separator;
	private static final String TEMP_EXCEL_FILE_PREFIX = "temp_excel_file_";

	public static final String EXCEL_EXTENSION = ".xlsx";
	public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public String generateExcelFile(ExcelContentProvider provider, String destination_path, String destination_filename) throws ExcelContentProviderException {

		try {
			if (provider == null) {
				throw new IllegalArgumentException("Invalid Excel content provider!");
			}

			// Generate the correct destination path
			String filename = "";
			if (destination_path == null || destination_path.trim().isEmpty()) {
				filename = TEMP_DIRECTORY;
			} else if (!destination_path.trim().endsWith(File.separator)) {
				filename = destination_path + File.separator;
			} else {
				filename = destination_path;
			}

			// Create the entire path in case it doesn't exist
			File directory = new File(filename);
			if (!directory.exists() || !directory.isDirectory()) {
				directory.mkdirs();
			}

			// Generate complete filename
			if (destination_filename == null || destination_filename.trim().isEmpty()) {
				filename += TEMP_EXCEL_FILE_PREFIX + System.currentTimeMillis() + EXCEL_EXTENSION;
			} else if (!destination_filename.trim().endsWith(EXCEL_EXTENSION)) {
				filename += destination_filename + EXCEL_EXTENSION;
			} else {
				filename += destination_filename;
			}

			// create a new file
			FileOutputStream out = new FileOutputStream(filename);
			// create a new workbook
			Workbook wb = new XSSFWorkbook();

			// Request the provider to add the content
			provider.addContent(wb, this);

			// write the workbook to the output stream
			wb.write(out);
			// close our file (don't blow out our file handles)
			out.close();
			wb.close();

			return filename;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelContentProviderException(e);
		}
	}

	public void createCell(Sheet sheet, Row row, int cell_index, CellStyle cell_style, Object value, int width) {
		Cell cell = row.createCell(cell_index);
		cell.setCellStyle(cell_style);
		if (value == null) {
			cell.setCellValue("--");
		} else {
			if (value instanceof String) {
				cell.setCellValue((String) value);
			}
			if (value instanceof Integer) {
				cell.setCellValue((Integer) value);
			}
			if (value instanceof Long) {
				cell.setCellValue((Long) value);
			}
			if (value instanceof Double) {
				cell.setCellValue((Double) value);
			}
			if (value instanceof Boolean) {
				if ((Boolean) value) {
					cell.setCellValue("Yes");
				} else {
					cell.setCellValue("No");
				}
			}
			if (width > 0) {
				sheet.setColumnWidth(cell_index, width);
			}
		}
	}
}
