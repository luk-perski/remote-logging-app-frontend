package utils.auth.oauth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {

	@JsonProperty("id")
	private String id;

	@JsonProperty("displayName")
	private String display_name;

	@JsonProperty("givenName")
	private String given_name;

	@JsonProperty("surname")
	private String surname;

	@JsonProperty("mail")
	private String mail;

	@JsonProperty("jobTitle")
	private String job_title;

	@JsonProperty("mobilePhone")
	private String mobile_phone;

	@JsonProperty("officeLocation")
	private String office_location;

	@JsonProperty("preferredLanguage")
	private String preferred_language;

	@JsonProperty("userPrincipalName")
	private String user_principal_name;

	@JsonProperty("businessPhones")
	private String[] business_phones;

	public UserProfile() {
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return display_name;
	}

	public void setDisplayName(String display_name) {
		this.display_name = display_name;
	}

	public String getGivenName() {
		return given_name;
	}

	public void setGivenName(String given_name) {
		this.given_name = given_name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getJobTitle() {
		return job_title;
	}

	public void setJobTitle(String job_title) {
		this.job_title = job_title;
	}

	public String getMobilePhone() {
		return mobile_phone;
	}

	public void setMobilePhone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getOfficeLocation() {
		return office_location;
	}

	public void setOfficeLocation(String office_location) {
		this.office_location = office_location;
	}

	public String getPreferredLanguage() {
		return preferred_language;
	}

	public void setPreferredLanguage(String preferred_language) {
		this.preferred_language = preferred_language;
	}

	public String getUserPrincipalName() {
		return user_principal_name;
	}

	public void setUserPrincipalName(String user_principal_name) {
		this.user_principal_name = user_principal_name;
	}

	public String[] getBusinessPhones() {
		return business_phones;
	}

	public void setBusinessPhones(String[] business_phones) {
		this.business_phones = business_phones;
	}
}
