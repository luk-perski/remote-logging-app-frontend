package utils.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import utils.word.exception.WordContentProviderException;

public interface WordContentProvider {

	public void addContent(XWPFDocument document, WordHelper wordHelper) throws WordContentProviderException;

}
