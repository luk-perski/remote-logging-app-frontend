package utils.auth.models;

import java.util.List;

import models.db.user.Role;
import models.db.user.User;

public class AuthenticatedUser {

	private User user;

	private Role role;

	private List<Role> possible_roles;

	private boolean admin_mode;

	private User admin_user;

	public AuthenticatedUser(User user, Role role, boolean admin_mode) {
		this.user = user;
		this.role = role;
		this.possible_roles = user.getPossibleRoles();
		this.admin_mode = admin_mode;
	}

	public AuthenticatedUser(User user, Role role, boolean admin_mode, User admin_user) {
		this.user = user;
		this.role = role;
		this.possible_roles = user.getPossibleRoles();
		this.admin_mode = admin_mode;
		this.admin_user = admin_user;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

	public List<Role> getPossibleRoles() {
		return possible_roles;
	}

	public boolean isAdminMode() {
		return admin_mode;
	}

	public User getAdminUser() {
		return admin_user;
	}
}
