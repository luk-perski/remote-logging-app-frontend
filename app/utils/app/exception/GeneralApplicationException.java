package utils.app.exception;

@SuppressWarnings("serial")
public class GeneralApplicationException extends Exception {

	public GeneralApplicationException() {
		super();
	}

	public GeneralApplicationException(String message) {
		super(message);
	}

	public GeneralApplicationException(Throwable cause) {
		super(cause);
	}

	public GeneralApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
