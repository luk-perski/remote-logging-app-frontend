package utils.auth.oauth.models;

public class AuthenticationStepResult {

	// A boolean that indicates if this result constitutes an error
	private boolean is_error;

	// If the previous property is true, then this property holds the error message
	private String error_message;

	// If this result step is not an error, then this result data property should hold the result object
	private Object result_data;

	public AuthenticationStepResult(boolean has_error, String error_message, Object result_data) {
		this.is_error = has_error;
		this.error_message = error_message;
		this.result_data = result_data;
	}

	public boolean isError() {
		return is_error;
	}

	public String getErrorMessage() {
		return error_message;
	}

	public Object getResultData() {
		return result_data;
	}

}
