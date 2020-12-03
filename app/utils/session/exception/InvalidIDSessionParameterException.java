package utils.session.exception;

@SuppressWarnings("serial")
public class InvalidIDSessionParameterException extends Exception {

	public InvalidIDSessionParameterException() {
		super();
	}

	public InvalidIDSessionParameterException(String message) {
		super(message);
	}

	public InvalidIDSessionParameterException(Throwable cause) {
		super(cause);
	}

	public InvalidIDSessionParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidIDSessionParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
