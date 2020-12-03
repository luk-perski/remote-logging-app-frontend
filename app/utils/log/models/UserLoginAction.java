package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import models.db.user.Role;

public class UserLoginAction implements LogAction {

	private String service;

	private int role;

	private String state;

	private boolean admin_mode;

	public UserLoginAction() {
	}

	public UserLoginAction(String service, int role, String state, boolean admin_mode) {
		this.service = service;
		this.role = role;
		this.state = state;
		this.admin_mode = admin_mode;
	}

	public String getService() {
		return service;
	}

	public int getRole() {
		return role;
	}

	public String getState() {
		return state;
	}

	public boolean isAdmin_mode() {
		return admin_mode;
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		Role role = Role.getByID(this.role);

		String description = "User logged in ";

		if (admin_mode) {
			description += "(ADMIN MODE) ";
		}

		if (role != null) {
			description += " as <b>" + role.getLabelEN() + "</b> ";
		}

		description += "with service <i>" + this.service + "</i> (state: " + this.state + ")";

		return description;
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "User Login";
	}
}
