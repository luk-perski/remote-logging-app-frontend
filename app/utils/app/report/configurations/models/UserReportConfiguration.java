package utils.app.report.configurations.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import models.db.user.Role;

public class UserReportConfiguration {

	public static final String FILTER_ROLE_PROPERTY = "filter_role";
	
	private Role filter_role;

	public UserReportConfiguration() {
	}

	public UserReportConfiguration(Role filter_role) {
		this.filter_role = filter_role;
	}

	@JsonProperty("filter_role")
	public Role getFilterRole() {
		return this.filter_role;
	}

	@JsonProperty("filter_role")
	public void setFilterRole(Role role) {
		this.filter_role = role;
	}
}
