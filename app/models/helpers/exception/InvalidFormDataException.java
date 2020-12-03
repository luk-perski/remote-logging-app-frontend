package models.helpers.exception;

@SuppressWarnings("serial")
public class InvalidFormDataException extends Exception {

	public InvalidFormDataException() {
		super();
	}

	public InvalidFormDataException(String message) {
		super(message);
	}

	public InvalidFormDataException(Throwable cause) {
		super(cause);
	}

	public InvalidFormDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFormDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
