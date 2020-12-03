package utils.pdf.providers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class BasicProviderHeaderAndFooter extends PdfPageEventHelper {

	private String header_text;

	private boolean add_on_first_page;

	private Font font;

	public BasicProviderHeaderAndFooter(String header_text, boolean add_on_first_page, Font font) {
		super();
		this.header_text = header_text;
		this.add_on_first_page = add_on_first_page;
		this.font = font;
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		if (this.add_on_first_page || writer.getPageNumber() > 1) {
			PdfContentByte content_byte = writer.getDirectContent();
			// Header content
			ColumnText.showTextAligned(content_byte, Element.ALIGN_LEFT, new Phrase(this.header_text, font), document.leftMargin(), document.top(), 0);
			// Footer content
			String footer_content = "" + writer.getPageNumber();
			ColumnText.showTextAligned(content_byte, Element.ALIGN_RIGHT, new Phrase(footer_content, font), document.right() - 2, document.bottom() - 20, 0);
		}
	}

}
