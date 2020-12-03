package utils.word;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import utils.word.exception.WordContentProviderException;

public class WordHelper {

	private static final String TEMP_DIRECTORY = "temp" + File.separator;
	private static final String TEMP_WORD_FILE_PREFIX = "temp_word_file_";
	private static final String WORD_EXTENSION = ".docx";

	public String generateWordFile(WordContentProvider provider, String destination_path, String destination_filename) throws WordContentProviderException {

		try {
			if (provider == null) {
				throw new IllegalArgumentException("Invalid Word content provider!");
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
				filename += TEMP_WORD_FILE_PREFIX + System.currentTimeMillis() + WORD_EXTENSION;
			} else if (!destination_filename.trim().endsWith(WORD_EXTENSION)) {
				filename += destination_filename + WORD_EXTENSION;
			} else {
				filename += destination_filename;
			}

			// create a new file
			FileOutputStream out = new FileOutputStream(filename);

			// create document
			XWPFDocument document = new XWPFDocument();

			// Request the provider to add the content
			provider.addContent(document, this);

			document.write(out);
			// Close the file
			out.close();

			return filename;

		} catch (Exception e) {
			e.printStackTrace();
			throw new WordContentProviderException(e);
		}
	}

	public void createEmptyParagraph(XWPFDocument document) {
		createParagraphWithText(document, "", false, false);
	}

	public void createParagraphWithText(XWPFDocument document, String text, boolean is_bold, boolean is_italic) {
		// Create paragraph
		XWPFParagraph paragraph = document.createParagraph();

		// You can enter the text or any object element, using Run. Using Paragraph instance you can create run.
		XWPFRun run = paragraph.createRun();
		run.setBold(is_bold);
		run.setItalic(is_italic);
		run.setText(text);
	}
}
