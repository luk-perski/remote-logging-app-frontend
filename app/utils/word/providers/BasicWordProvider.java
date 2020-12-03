package utils.word.providers;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import utils.word.WordContentProvider;
import utils.word.WordHelper;
import utils.word.exception.WordContentProviderException;

public class BasicWordProvider implements WordContentProvider {

	@Override
	public void addContent(XWPFDocument document, WordHelper wordHelper) throws WordContentProviderException {
		try {

			String text = "This is the text on the first paragraph of the document. It is a set of sentences that are all very short. But all together, they make up for a complete paragraph.";
			wordHelper.createParagraphWithText(document, text, false, false);
			wordHelper.createEmptyParagraph(document);

			text = "This is a paragraph with bold text.";
			wordHelper.createParagraphWithText(document, text, true, false);
			wordHelper.createEmptyParagraph(document);

			text = "And this is a paragraph with italic text.";
			wordHelper.createParagraphWithText(document, text, false, true);
			wordHelper.createEmptyParagraph(document);

			text = "And how about a paragraph with both bold and italic text.";
			wordHelper.createParagraphWithText(document, text, true, true);
			wordHelper.createEmptyParagraph(document);

			// create table
			XWPFTable table = document.createTable();

			// create first row
			XWPFTableRow tableRowOne = table.getRow(0);
			tableRowOne.getCell(0).setText("col one, row one");
			tableRowOne.addNewTableCell().setText("col two, row one");
			tableRowOne.addNewTableCell().setText("col three, row one");

			// create second row
			XWPFTableRow tableRowTwo = table.createRow();
			tableRowTwo.getCell(0).setText("col one, row two");
			tableRowTwo.getCell(1).setText("col two, row two");
			tableRowTwo.getCell(2).setText("col three, row two");

			// create third row
			XWPFTableRow tableRowThree = table.createRow();
			tableRowThree.getCell(0).setText("col one, row three");
			tableRowThree.getCell(1).setText("col two, row three");
			tableRowThree.getCell(2).setText("col three, row three");

		} catch (Exception e) {
			throw new WordContentProviderException(e);
		}
	}
}
