package models.db.app.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import controllers.AuthenticationController;
import io.ebean.Finder;
import io.ebean.Model;
import models.db.user.Role;
import play.mvc.Http.RequestHeader;
import utils.Utils;
import utils.app.menu.ApplicationMenuCustomValidator;

@Entity
@Table(name = "app_menu_application_menu")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class ApplicationMenu extends Model {

	private static final Logger log = LoggerFactory.getLogger(ApplicationMenu.class);

	public static final int MAX_SIZE_LABEL = 50;
	public static final int MAX_SIZE_SHORT_LABEL = 30;
	public static final int MAX_SIZE_URL = 200;
	public static final int MAX_SIZE_CUSTOM_VALIDATION_CLASS = 250;

	@Id
	public Integer id;

	// If this menu is an option within another menu, this represents the connection to the parent
	@ManyToOne
	private ApplicationMenu parent;

	@Column(length = MAX_SIZE_LABEL, nullable = false)
	private String label_pt;

	@Column(length = MAX_SIZE_LABEL, nullable = false)
	private String label_en;

	@Column(length = MAX_SIZE_SHORT_LABEL, nullable = false)
	private String short_label_pt;

	@Column(length = MAX_SIZE_SHORT_LABEL, nullable = false)
	private String short_label_en;

	// This is the URL that the menu option points to.
	// It's optional, since the menu can itself be also a container for other menu options
	@Column(length = MAX_SIZE_URL)
	private String url;

	// If there's an icon associated with this menu/option, set the CSS class here
	private String icon_css_class;

	// Whether or not the menu/option is currently active
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_active;

	// whether or not this menu/option is accessible to the public
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_public;

	// whether or not this menu/option is to be shown when the user is authenticated
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean show_when_authenticated;

	// If this option is part of a set of options within a menu, this states the order in which the option should appear in the menu
	@Column(nullable = false, columnDefinition = "INT DEFAULT 1")
	private Integer order_within_parent;

	// Whether or not a divider should be placed before this option
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean has_divider_before;

	// Whether or not this menu should be opened by default (if it has children)
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean opened_by_default;

	// An optional Java class that handles the custom validation of the menu/option
	// This is used in the cases where the simple validation of roles is not enough to decide whether or not this menu/option should be shown
	@Column(length = MAX_SIZE_CUSTOM_VALIDATION_CLASS)
	private String custom_validation_class;

	// The list of options in this menu
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
	@OrderBy(value = "order_within_parent")
	private List<ApplicationMenu> children;

	// The set of roles (for authenticated users) that can access this menu/option
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "menu")
	private List<ApplicationMenuRole> roles;

	private static final Finder<Integer, ApplicationMenu> finder = new Finder<Integer, ApplicationMenu>(ApplicationMenu.class);

	public static ApplicationMenu getByID(Integer id) {
		return (id == null) ? null : finder.byId(id);
	}

	public static List<ApplicationMenu> getRootMenus() {
		// Root menus are those that have no parent menu
		return finder.query().where().isNull("parent").findList();
	}

	public static void updateMenuRoleAssociations(List<Role> all_roles) {
		List<ApplicationMenu> all_menus = finder.all();
		if (all_menus != null) {
			for (ApplicationMenu menu : all_menus) {
				menu.updateRoleAssociations(all_roles);
			}
		}
	}

	public ApplicationMenu() {
	}

	public ApplicationMenu(int id, ApplicationMenu parent, String label_pt, String label_en, String short_label_pt, String short_label_en, String url, String icon_css_class, List<ApplicationMenu> children, Boolean is_active, Boolean is_public, Boolean show_when_authenticated, Integer order_within_parent, Boolean has_divider_before, Boolean opened_by_default, List<ApplicationMenuRole> roles) {
		this.id = id;
		this.parent = parent;
		this.label_pt = label_pt;
		this.label_en = label_en;
		this.short_label_pt = short_label_pt;
		this.short_label_en = short_label_en;
		this.url = url;
		this.icon_css_class = icon_css_class;
		this.children = children;
		this.is_active = is_active;
		this.is_public = is_public;
		this.show_when_authenticated = show_when_authenticated;
		this.order_within_parent = order_within_parent;
		this.has_divider_before = has_divider_before;
		this.opened_by_default = opened_by_default;
		this.roles = roles;
	}

	/**
	 * Get the list of options from a specific menu, based on the user's current situation (is authenticated and with which role)
	 * 
	 * @param request
	 *            The HTTP request (for context)
	 * @param utils
	 *            The set of utils (for accessing session details)
	 * @return The list of options for this menu that the current user has access to
	 */
	@JsonIgnore
	public List<ApplicationMenu> getContextualChildren(RequestHeader request, Utils utils) {
		if (children != null) {
			List<ApplicationMenu> options = new ArrayList<ApplicationMenu>();
			for (ApplicationMenu option : children) {
				if (option.canBeShownInContext(request, utils)) {
					options.add(option);
				}
			}
			return options;
		}
		return null;
	}

	/**
	 * Check if this menu/option can be shown based on the user's current situation
	 * 
	 * @param request
	 *            The HTTP request (for context)
	 * @param utils
	 *            The set of Utils (for acessing session details)
	 * @return True if the menu/option can be shown, false otherwise
	 */
	@JsonIgnore
	public boolean canBeShownInContext(RequestHeader request, Utils utils) {
		if (this.isActive()) {
			// First check if the option is public
			if (this.isPublic()) {
				// If it's public, check if the user is not authenticated (because if so, we need to check if the option is supposed to be
				// shown when authenticated)
				if (!utils.session_manager.isAuthenticated(request) || this.showWhenAuthenticated()) {
					return this.passesCustomValidation(request, utils);
				}
			} else {
				// It's not public. Check if it can be shown while the user is authenticated and if the user is actually authenticated
				if (this.showWhenAuthenticated() && utils.session_manager.isAuthenticated(request)) {
					// Check if the role that the user currently has is allowed to access the option
					String auth_role_id = utils.session_manager.getAuthenticatedUserSessionParameter(request, AuthenticationController.ROLE_KEY);
					if (this.roles != null && !this.roles.isEmpty() && auth_role_id != null) {
						for (ApplicationMenuRole role_association : this.roles) {
							if (role_association.getRole() != null) {
								if (role_association.getRole().getID().toString().equals(auth_role_id)) {
									if (role_association.hasAccess()) {
										return this.passesCustomValidation(request, utils);
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * If the menu/option has a custom validator class, checks if it passes that validation
	 * 
	 * @param request
	 *            The HTTP request (for context)
	 * @param utils
	 *            The set of Utils (for acessing session details)
	 * @return True if the menu/option passes the custom validation, false otherwise
	 */
	@JsonIgnore
	private boolean passesCustomValidation(RequestHeader request, Utils utils) {
		if (this.custom_validation_class != null && !this.custom_validation_class.trim().isEmpty()) {
			try {
				ApplicationMenuCustomValidator validator = (ApplicationMenuCustomValidator) Class.forName(this.custom_validation_class).newInstance();
				if (validator != null) {
					return validator.passesCustomValidation(request, utils);
				}
			} catch (Exception warning) {
				log.warn("EXCEPTION: " + warning.getMessage());
				return false;
			}
		}
		return true;
	}

	@JsonIgnore
	public ApplicationMenu getParent() {
		return parent;
	}

	@JsonProperty("parent_id")
	public Integer getParentID() {
		if (this.parent != null) {
			return parent.id;
		}
		return null;
	}

	@JsonProperty("id")
	public Integer getID() {
		return this.id;
	}

	@JsonProperty("label_pt")
	public String getLabelPT() {
		return label_pt;
	}

	@JsonProperty("label_en")
	public String getLabelEN() {
		return label_en;
	}

	@JsonProperty("short_label_pt")
	public String getShortLabelPT() {
		return short_label_pt;
	}

	@JsonProperty("short_label_en")
	public String getShortLabelEN() {
		return short_label_en;
	}

	@JsonProperty("url")
	public String getURL() {
		return url;
	}

	@JsonProperty("icon_css_class")
	public String getIconCSSClass() {
		return this.icon_css_class;
	}

	@JsonProperty("custom_validation_class")
	public String getCustomValidationClass() {
		return this.custom_validation_class;
	}

	@JsonProperty("is_active")
	public Boolean isActive() {
		return is_active;
	}

	@JsonProperty("is_public")
	public Boolean isPublic() {
		return is_public;
	}

	@JsonProperty("show_when_authenticated")
	public Boolean showWhenAuthenticated() {
		return show_when_authenticated;
	}

	@JsonProperty("order_within_parent")
	public int getOrderWithinParent() {
		return this.order_within_parent;
	}

	@JsonProperty("has_divider_before")
	public boolean hasDividerBefore() {
		return this.has_divider_before != null && this.has_divider_before;
	}

	@JsonProperty("opened_by_default")
	public boolean openedByDefault() {
		return this.opened_by_default != null && this.opened_by_default;
	}

	@JsonProperty("children")
	public List<ApplicationMenu> getChildren() {
		return this.children;
	}

	@JsonProperty("roles")
	public List<ApplicationMenuRole> getRoles() {
		return roles;
	}

	public void setParent(ApplicationMenu parent) {
		this.parent = parent;
	}

	public void setLabelPT(String label_pt) {
		this.label_pt = label_pt;
	}

	public void setLabelEN(String label_en) {
		this.label_en = label_en;
	}

	public void setShortLabelPT(String short_label_pt) {
		this.short_label_pt = short_label_pt;
	}

	public void setShortLabelEN(String short_label_en) {
		this.short_label_en = short_label_en;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void setIconCSSClass(String icon_css_class) {
		this.icon_css_class = icon_css_class;
	}

	public void setChildren(List<ApplicationMenu> children) {
		this.children = children;
	}

	public void setIsActive(boolean is_active) {
		this.is_active = is_active;
	}

	public void setIsPublic(boolean is_public) {
		this.is_public = is_public;
	}

	public void setShowWhenAuthenticated(boolean show_when_authenticated) {
		this.show_when_authenticated = show_when_authenticated;
	}

	public void setOrderWithinParent(int order) {
		this.order_within_parent = order;
	}

	public void setCustomValidationClass(String custom_validation_class) {
		this.custom_validation_class = custom_validation_class;
	}

	public void setHasDividerBefore(boolean has_divider_before) {
		this.has_divider_before = has_divider_before;
	}

	public void setOpenedByDefault(boolean opened_by_default) {
		this.opened_by_default = opened_by_default;
	}

	public void setRoles(List<ApplicationMenuRole> roles) {
		this.roles = roles;
	}

	private void updateRoleAssociations(List<Role> all_roles) {
		if (all_roles != null) {
			for (Role role : all_roles) {
				if (!this.hasRoleAssociation(role)) {
					new ApplicationMenuRole(this, role, false).save();
				}
			}
		}
		if (this.children != null) {
			for (ApplicationMenu menu : this.children) {
				menu.updateRoleAssociations(all_roles);
			}
		}
	}

	private boolean hasRoleAssociation(Role role) {
		return ApplicationMenuRole.getByMenuForRole(this.getID(), role.getID()) != null;
	}

	public void updateRoleAssociation(Role role, boolean has_access) {
		if (role != null) {
			ApplicationMenuRole association = ApplicationMenuRole.getByMenuForRole(this.getID(), role.getID());
			if (association == null) {
				association = new ApplicationMenuRole(this, role, has_access);
			} else {
				association.setHasAccess(has_access);
			}
			association.save();
		}
	}
}
