package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApplicationGeneralError implements LogAction {

	private String error_log;

	public ApplicationGeneralError() {
	}

	public ApplicationGeneralError(String error_log) {
		this.error_log = error_log;
	}

	public String getError_log() {
		return this.error_log;
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		return this.getError_log();
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "Application General Error";
	}
}
