package utils.auth.exception;

@SuppressWarnings("serial")
public class UserCannotHaveRoleException extends Exception {

	public UserCannotHaveRoleException() {
		super();
	}

	public UserCannotHaveRoleException(String message) {
		super(message);
	}

	public UserCannotHaveRoleException(Throwable cause) {
		super(cause);
	}

	public UserCannotHaveRoleException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserCannotHaveRoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
