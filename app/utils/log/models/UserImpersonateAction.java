package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import models.db.user.User;

public class UserImpersonateAction implements LogAction {

	private long impersonate_user_id;

	public UserImpersonateAction() {
	}

	public UserImpersonateAction(Long impersonate_user_id) {
		if (impersonate_user_id != null) {
			this.impersonate_user_id = impersonate_user_id;
		}
	}

	public long getImpersonate_user_id() {
		return this.impersonate_user_id;
	}

	@Override
	@JsonIgnore
	public String getDescription() {

		String description = "User started impersonation of another user ";

		User user = User.getByID(this.impersonate_user_id);
		if (user != null) {
			description += "(" + user.getID() + " - " + user.getUsername() + " - " + user.getDisplayName() + ")";
		}

		return description;
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "User Impersonate";
	}
}
