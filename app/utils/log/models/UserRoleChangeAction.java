package utils.log.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import models.db.log.UserLog;
import models.db.user.Role;

public class UserRoleChangeAction implements LogAction {

	private int previous_role;

	private int new_role;

	public UserRoleChangeAction() {
	}

	public UserRoleChangeAction(int previous_role, int new_role) {
		this.previous_role = previous_role;
		this.new_role = new_role;
	}

	public int getPrevious_role() {
		return previous_role;
	}

	public int getNew_role() {
		return new_role;
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		Role previous_role = Role.getByID(this.previous_role);
		Role new_role = Role.getByID(this.new_role);
		if (previous_role != null && new_role != null) {
			return "User changed roles: " + previous_role.getLabelEN() + " <i class=\"fa fa-arrow-right\"></i> " + new_role.getLabelEN();
		} else {
			return UserLog.INVALID_INFO;
		}
	}

	@Override
	@JsonIgnore
	public String getTypeLabel() {
		return "User Role Change";
	}
}
