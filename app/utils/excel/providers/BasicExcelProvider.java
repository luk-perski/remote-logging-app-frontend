package utils.excel.providers;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import utils.excel.ExcelContentProvider;
import utils.excel.ExcelHelper;
import utils.excel.exception.ExcelContentProviderException;

public class BasicExcelProvider implements ExcelContentProvider {

	@Override
	public void addContent(Workbook workbook, ExcelHelper excel_helper) throws ExcelContentProviderException {
		try {

			// Create a new sheet
			Sheet sheet1 = workbook.createSheet();
			int sheet_index = 0;
			// Set sheet name
			workbook.setSheetName(sheet_index, "Sheet1");

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
			row = sheet1.createRow(row_index);
			// If necessary to adjust the Height of the row, uncomment the following
			// row.setHeight((short) 0x250);

			// Actually create the cell with some content
			int cell_index = 0;
			int cell_width = 8000;
			String cell_content = "This is the text on the first cell of the first row";
			excel_helper.createCell(sheet1, row, cell_index, cell_style_normal, cell_content, cell_width);
			cell_content = "This is the text on the second cell of the first row";
			excel_helper.createCell(sheet1, row, ++cell_index, cell_style_normal, cell_content, cell_width);

			// Create new row (right below the previous one)
			row = sheet1.createRow(++row_index);

			// Create cell in the second row
			cell_index = 0;
			cell_content = "This is the bold text on the first cell of the second row";
			excel_helper.createCell(sheet1, row, cell_index, cell_style_bold, cell_content, cell_width);

			// Create a new sheet
			Sheet sheet2 = workbook.createSheet();
			// Set sheet name
			workbook.setSheetName(++sheet_index, "Sheet2");

			// Create a row instance in sheet 2 (at index 0 - first row)
			row_index = 0;
			row = sheet2.createRow(row_index);

			// Actually create the cell with some content
			cell_index = 0;
			cell_content = "This is the text on the first cell of the first row";
			excel_helper.createCell(sheet1, row, cell_index, cell_style_normal, cell_content, cell_width);

		} catch (Exception e) {
			throw new ExcelContentProviderException(e);
		}
	}
}
