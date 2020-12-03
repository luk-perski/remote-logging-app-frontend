package utils.pdf.exception;

@SuppressWarnings("serial")
public class PDFContentProviderException extends Exception {

	public PDFContentProviderException() {
		super();
	}

	public PDFContentProviderException(String message) {
		super(message);
	}

	public PDFContentProviderException(Throwable cause) {
		super(cause);
	}

	public PDFContentProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public PDFContentProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
