package utils.pdf;

import com.itextpdf.text.Rectangle;

public class PDFContentProviderConfiguration {

	private Rectangle page_size;

	private int margin_left;
	private int margin_right;
	private int margin_top;
	private int margin_bottom;

	private String metadata_title;
	private String metadata_subject;
	private String metadata_keywords;
	private String metadata_author;
	private String metadata_creator;

	public String getMetadataTitle() {
		return metadata_title;
	}

	public void setMetadataTitle(String metadata_title) {
		this.metadata_title = metadata_title;
	}

	public String getMetadataSubject() {
		return metadata_subject;
	}

	public void setMetadataSubject(String metadata_subject) {
		this.metadata_subject = metadata_subject;
	}

	public String getMetadataKeywords() {
		return metadata_keywords;
	}

	public void setMetadataKeywords(String metadata_keywords) {
		this.metadata_keywords = metadata_keywords;
	}

	public String getMetadataAuthor() {
		return metadata_author;
	}

	public void setMetadataAuthor(String metadata_author) {
		this.metadata_author = metadata_author;
	}

	public String getMetadataCreator() {
		return metadata_creator;
	}

	public void setMetadataCreator(String metadata_creator) {
		this.metadata_creator = metadata_creator;
	}

	public void setPageSize(Rectangle size) {
		this.page_size = size;
	}

	public Rectangle getPageSize() {
		return page_size;
	}

	public int getMarginLeft() {
		return margin_left;
	}

	public void setMarginLeft(int margin_left) {
		this.margin_left = margin_left;
	}

	public int getMarginRight() {
		return margin_right;
	}

	public void setMarginRight(int margin_right) {
		this.margin_right = margin_right;
	}

	public int getMarginTop() {
		return margin_top;
	}

	public void setMarginTop(int margin_top) {
		this.margin_top = margin_top;
	}

	public int getMarginBottom() {
		return margin_bottom;
	}

	public void setMarginBottom(int margin_bottom) {
		this.margin_bottom = margin_bottom;
	}
}
