package utils.excel;

import org.apache.poi.ss.usermodel.Workbook;

import utils.excel.exception.ExcelContentProviderException;

public interface ExcelContentProvider {

	public void addContent(Workbook wb, ExcelHelper excel_helper) throws ExcelContentProviderException;

}
