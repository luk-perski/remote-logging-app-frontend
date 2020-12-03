package utils.app.exception;

@SuppressWarnings("serial")
public class ReportExecutionException extends Exception {

	public ReportExecutionException() {
	}

	public ReportExecutionException(String message) {
		super(message);
	}

	public ReportExecutionException(Throwable cause) {
		super(cause);
	}

	public ReportExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
