package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserLogoutAction implements LogAction {

	public UserLogoutAction() {
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		return "User logged out of the system";
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "User Logout";
	}

}
