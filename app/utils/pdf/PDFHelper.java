package utils.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import utils.pdf.exception.PDFContentProviderException;

public class PDFHelper {

	private static final String TEMP_DIRECTORY = "temp" + File.separator;
	private static final String TEMP_PDF_FILE_PREFIX = "temp_pdf_file_";
	private static final String PDF_EXTENSION = ".pdf";

	public String generatePDFFile(PDFContentProvider provider, String destination_path, String destination_filename) throws PDFContentProviderException {

		try {
			if (provider == null) {
				throw new IllegalArgumentException("Invalid PDF content provider!");
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
				filename += TEMP_PDF_FILE_PREFIX + System.currentTimeMillis() + PDF_EXTENSION;
			} else if (!destination_filename.trim().endsWith(PDF_EXTENSION)) {
				filename += destination_filename + PDF_EXTENSION;
			} else {
				filename += destination_filename;
			}

			// Retrieve the necessary configuration from the provider
			PDFContentProviderConfiguration configuration = provider.getConfiguration();

			// Generate the document and the PDF writer instance
			Document document = new Document(configuration.getPageSize(), configuration.getMarginLeft(), configuration.getMarginRight(), configuration.getMarginTop(), configuration.getMarginBottom());
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

			// Allow the content provider to do some font initialization
			provider.initializeFonts();

			// If a page event (ex: header and footer) was configured, add it here
			PdfPageEventHelper page_event = provider.getPageEvent();
			if (page_event != null) {
				writer.setPageEvent(page_event);
			}

			// Open the document
			document.open();

			// Add Metadata
			if (configuration.getMetadataTitle() != null) {
				document.addTitle(configuration.getMetadataTitle());
			}
			if (configuration.getMetadataSubject() != null) {
				document.addSubject(configuration.getMetadataSubject());
			}
			if (configuration.getMetadataKeywords() != null) {
				document.addKeywords(configuration.getMetadataKeywords());
			}
			if (configuration.getMetadataAuthor() != null) {
				document.addAuthor(configuration.getMetadataAuthor());
			}
			if (configuration.getMetadataCreator() != null) {
				document.addCreator(configuration.getMetadataCreator());
			}

			provider.addContent(document);

			// Close the document
			document.close();

			return filename;

		} catch (FileNotFoundException e) {
			throw new PDFContentProviderException(e);
		} catch (DocumentException e) {
			throw new PDFContentProviderException(e);
		}
	}
}
