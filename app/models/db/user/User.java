package models.db.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.Index;
import models.db.app.files.ResourceAssociatedFile;
import models.db.app.files.ResourceAssociatedFileType;
import models.db.remote.logging.Team;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http.Request;
import utils.auth.oauth.models.OAuthUserInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_user")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class User extends Model {

	private static final Logger log = LoggerFactory.getLogger(User.class);

	public static final int MAX_SIZE_NAME = 250;
	public static final int MAX_SIZE_USERNAME = 50;
	public static final int MAX_SIZE_EMAIL = 100;
	public static final int MAX_SIZE_PASSWORD = 200;

	@Id
	private Long id;

	@Column(length = MAX_SIZE_NAME, nullable = false)
	private String name;

	@Column(length = MAX_SIZE_NAME)
	private String display_name;

	@Index
	@Column(length = MAX_SIZE_USERNAME, nullable = false, unique = true)
	private String username;

	@Column(length = MAX_SIZE_EMAIL, nullable = false, unique = true)
	private String email;

	// This is the password to be used in this system authentication process
	// If the system uses an external authentication process, then this password should not be used (except for temporary access)
	private String local_pwd;

	// The roles that the user can have in the system
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<UserRole> roles;

	@ManyToOne
	private Team team;

	private static final Finder<Long, User> finder = new Finder<Long, User>(User.class);

	public static User getByID(Long id) {
		return (id == null) ? null : finder.byId(id);
	}

	public static User getByUsername(String username) {
		return (username == null) ? null : finder.query().where().eq("username", username).setMaxRows(1).findOne();
	}

	public static User getByEmail(String email) {
		return (email == null) ? null : finder.query().where().eq("email", email).setMaxRows(1).findOne();
	}

	/**
	 * 
	 * @param oauth_user_info
	 * @return
	 */
	public static User getUserFromOAuthUserInfo(OAuthUserInfo oauth_user_info) {
		if (oauth_user_info != null) {
			User user = User.getByUsername(oauth_user_info.getUsername());
			if (user == null) {
				log.trace("User with username '" + oauth_user_info.getUsername() + "' doesn't exist. Creating...");
				// User doesn't exist, create them
				user = new User();
				user.setUsername(oauth_user_info.getUsername());
			} else {
				log.trace("User with username '" + user.getUsername() + "' already exists.");
			}
			user.setEmail(oauth_user_info.getEmail());
			user.setName(oauth_user_info.getName());
			user.setDisplayName(oauth_user_info.getDisplayName());
			user.save();
			user.refresh();

			// Update user OAuth info
			log.trace("Updating user OAuth info...");
			UserOAuth.updateDataByUser(user, oauth_user_info);

			return user;
		}
		return null;
	}

	/**
	 * Set the new password for the user. The password will first be encrypted using BCrypt.
	 * 
	 * @param new_password
	 *            The password to be stored
	 * @throws IllegalArgumentException
	 *             If the new password is null or empty
	 */
	public void setPassword(String new_password) throws IllegalArgumentException {
		if (new_password == null || new_password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		String hashed_password = BCrypt.hashpw(new_password, BCrypt.gensalt());
		if (hashed_password == null || hashed_password.length() > MAX_SIZE_PASSWORD) {
			throw new IllegalArgumentException("Password is not valid");
		}
		this.local_pwd = hashed_password;
	}

	/**
	 * Authenticate user with local password. Authentication method using a local password field in the DB. The password digest (with BCrypt) is
	 * stored in the DB
	 * 
	 * @param password
	 *            The password to test
	 * @return true if correct, false if not
	 */
	public boolean authenticate(String password) {
		if (password != null && !password.trim().isEmpty()) {
			if (this.local_pwd != null && !this.local_pwd.isEmpty()) {
				// Compare input-password with digest-password
				return BCrypt.checkpw(password, this.local_pwd);
			}
		}
		return false;
	}

	/**
	 * Get the list of roles that this user can have
	 * 
	 * @return A list of roles that this user can change into
	 */
	@JsonIgnore
	public List<Role> getPossibleRoles() {
		if (this.roles != null) {
			List<Role> roles = new ArrayList<Role>();

			for (UserRole user_role : this.roles) {
				if (user_role != null && user_role.getRole() != null) {
					if (user_role.isActive()) {
						roles.add(user_role.getRole());
					}
				}
			}

			return roles;
		}
		return null;
	}

	/**
	 * Get the default role to assign to the user. This is the first active role in the list of possible roles for the user
	 * 
	 * @return The Role that is considered the default for the user. Null if the user has no assigned active roles
	 */
	@JsonIgnore
	public Role getDefaultRole() {
		List<Role> possible_roles = this.getPossibleRoles();
		if (possible_roles != null && !possible_roles.isEmpty()) {
			return possible_roles.get(0);
		}
		return null;
	}

	/**
	 * Check if the user can have a certain role assigned
	 * 
	 * @param role
	 *            The role to test
	 * @return True if this role can be assigned to the user. False otherwise.
	 */
	public boolean canHaveRole(Role role) {
		if (role != null) {
			UserRole record = UserRole.getByUserForRole(this.id, role.getID());
			return record != null && record.isActive();
		}
		return false;
	}

	public boolean hasRole(Role role) {
		return this.canHaveRole(role);
	}

	public boolean hasMultipleRoles() {
		return this.roles != null && this.roles.size() > 1;
	}

	public Long getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		if (this.display_name != null && !this.display_name.trim().isEmpty()) {
			return this.display_name;
		}

		return this.getName();
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String name) {
		this.display_name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void updateRoleAssociation(Role role, boolean is_active) {
		if (role != null) {
			UserRole record = UserRole.getByUserForRole(this.getID(), role.getID());
			if (record == null) {
				record = new UserRole(this, role, is_active);
			} else {
				record.setIsActive(is_active);
			}
			record.save();
		}
	}


	private ResourceAssociatedFile getUserPhoto() {
		List<ResourceAssociatedFile> files = ResourceAssociatedFile.getByResourceAndType(this.getClass().getCanonicalName(), this.id.toString(), ResourceAssociatedFileType.USER_PHOTO);
		if (files != null && !files.isEmpty()) {
			// return the first user photo that was retrieved
			return files.get(0);
		}
		return null;
	}

	public OAuthUserInfo getOAuthUserInfo() {
		UserOAuth user_oauth = UserOAuth.getByUserID(this.id);
		if (user_oauth != null) {
			return user_oauth.getData();
		}
		return null;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
