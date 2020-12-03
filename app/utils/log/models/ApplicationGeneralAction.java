package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApplicationGeneralAction implements LogAction {

	private String action_log;

	public ApplicationGeneralAction() {
	}

	public ApplicationGeneralAction(String action_log) {
		this.action_log = action_log;
	}

	public String getAction_log() {
		return this.action_log;
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		return this.getAction_log();
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "Application General Action";
	}
}
