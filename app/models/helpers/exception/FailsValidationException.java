package models.helpers.exception;

@SuppressWarnings("serial")
public class FailsValidationException extends Exception {

	public FailsValidationException() {
		super();
	}

	public FailsValidationException(String message) {
		super(message);
	}

	public FailsValidationException(Throwable cause) {
		super(cause);
	}

	public FailsValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailsValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
