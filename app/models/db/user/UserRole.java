package models.db.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "user_user_role")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class UserRole extends Model {

	@Id
	private Long id;

	@JsonIgnore
	@ManyToOne
	@Column(nullable = false)
	private User user;

	@ManyToOne
	@Column(nullable = false)
	private Role role;

	// If this role is currently active for this user
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_active;

	private static final Finder<Long, UserRole> finder = new Finder<Long, UserRole>(UserRole.class);

	public static UserRole getByUserForRole(Long user_id, Integer role_id) {
		return finder.query().where().eq("user.id", user_id).eq("role.id", role_id).setMaxRows(1).findOne();
	}

	public UserRole(User user, Role role, boolean is_active) {
		this.user = user;
		this.role = role;
		this.is_active = is_active;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isActive() {
		return this.is_active != null && this.is_active;
	}

	public void setIsActive(Boolean is_active) {
		this.is_active = is_active;
	}
}
