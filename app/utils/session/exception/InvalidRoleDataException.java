package utils.session.exception;

@SuppressWarnings("serial")
public class InvalidRoleDataException extends Exception {

	public InvalidRoleDataException() {
		super();
	}

	public InvalidRoleDataException(String message) {
		super(message);
	}

	public InvalidRoleDataException(Throwable cause) {
		super(cause);
	}

	public InvalidRoleDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRoleDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
