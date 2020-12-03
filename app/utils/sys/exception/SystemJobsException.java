package utils.sys.exception;

@SuppressWarnings("serial")
public class SystemJobsException extends Exception {

	public SystemJobsException() {
		super();
	}

	public SystemJobsException(String message) {
		super(message);
	}

	public SystemJobsException(Throwable cause) {
		super(cause);
	}

	public SystemJobsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemJobsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
