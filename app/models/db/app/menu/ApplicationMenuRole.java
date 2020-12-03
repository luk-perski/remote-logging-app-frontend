package models.db.app.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ebean.Finder;
import io.ebean.Model;
import models.db.user.Role;

@Entity
@Table(name = "app_menu_application_menu_role")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class ApplicationMenuRole extends Model {

	@Id
	private Integer id;

	@ManyToOne
	@Column(nullable = false)
	private ApplicationMenu menu;

	@ManyToOne
	@Column(nullable = false)
	private Role role;

	// Whether or not this menu/option is accessible by this role
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean has_access;

	private static final Finder<Integer, ApplicationMenuRole> finder = new Finder<Integer, ApplicationMenuRole>(ApplicationMenuRole.class);

	public ApplicationMenuRole(ApplicationMenu menu, Role role, boolean has_access) {
		this.menu = menu;
		this.role = role;
		this.has_access = has_access;
	}

	@JsonIgnore
	public ApplicationMenu getMenu() {
		return menu;
	}

	public Role getRole() {
		return role;
	}

	@JsonProperty("has_access")
	public boolean hasAccess() {
		return this.has_access;
	}

	public void setHasAccess(boolean has_access) {
		this.has_access = has_access;
	}

	public static ApplicationMenuRole getByMenuForRole(Integer menu_id, Integer role_id) {
		if (menu_id == null || role_id == null) {
			return null;
		}
		return finder.query().where().eq("menu.id", menu_id).eq("role.id", role_id).setMaxRows(1).findOne();
	}

}
