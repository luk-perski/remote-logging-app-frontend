package utils.excel.exception;

@SuppressWarnings("serial")
public class ExcelContentProviderException extends Exception {

	public ExcelContentProviderException() {
		super();
	}

	public ExcelContentProviderException(String message) {
		super(message);
	}

	public ExcelContentProviderException(Throwable cause) {
		super(cause);
	}

	public ExcelContentProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelContentProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
