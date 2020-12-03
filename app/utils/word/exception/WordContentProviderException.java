package utils.word.exception;

@SuppressWarnings("serial")
public class WordContentProviderException extends Exception {

	public WordContentProviderException() {
		super();
	}

	public WordContentProviderException(String message) {
		super(message);
	}

	public WordContentProviderException(Throwable cause) {
		super(cause);
	}

	public WordContentProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public WordContentProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
