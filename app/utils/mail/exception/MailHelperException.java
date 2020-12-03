package utils.mail.exception;

@SuppressWarnings("serial")
public class MailHelperException extends Exception {

	public MailHelperException() {
		super();
	}

	public MailHelperException(String message) {
		super(message);
	}

	public MailHelperException(Throwable cause) {
		super(cause);
	}

	public MailHelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
