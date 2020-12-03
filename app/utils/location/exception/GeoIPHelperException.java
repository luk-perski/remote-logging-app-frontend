package utils.location.exception;

@SuppressWarnings("serial")
public class GeoIPHelperException extends Exception {

	public GeoIPHelperException() {
		super();
	}

	public GeoIPHelperException(String message) {
		super(message);
	}

	public GeoIPHelperException(Throwable cause) {
		super(cause);
	}

	public GeoIPHelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeoIPHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
