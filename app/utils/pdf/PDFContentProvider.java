package utils.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;

import utils.pdf.exception.PDFContentProviderException;

public interface PDFContentProvider {

	public PDFContentProviderConfiguration getConfiguration();

	public void initializeFonts() throws PDFContentProviderException;

	public PdfPageEventHelper getPageEvent();

	public void addContent(Document document) throws PDFContentProviderException;

}
