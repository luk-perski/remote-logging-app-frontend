package utils.auth.oauth.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthUserInfo {

	private String service;

	private String authorization_code;

	private String state;

	private String access_token;

	private String access_token_type;

	private Integer expires_in;

	private String id_token;

	private String user_id;

	private String username;

	private String email;

	private String name;

	private String display_name;

	private String given_name;

	private String family_name;

	private String locale;

	private String picture_url;

	public OAuthUserInfo() {
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@JsonProperty("authorization_code")
	public String getAuthorizationCode() {
		return authorization_code;
	}

	public void setAuthorizationCode(String authorization_code) {
		this.authorization_code = authorization_code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("access_token")
	public String getAccessToken() {
		return access_token;
	}

	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}

	@JsonProperty("access_token_type")
	public String getAccessTokenType() {
		return access_token_type;
	}

	public void setAccessTokenType(String access_token_type) {
		this.access_token_type = access_token_type;
	}

	@JsonProperty("expires_in")
	public Integer getExpiresIn() {
		return expires_in;
	}

	public void setExpiresIn(Integer expires_in) {
		this.expires_in = expires_in;
	}

	@JsonProperty("id_token")
	public String getIDToken() {
		return id_token;
	}

	public void setIDToken(String id_token) {
		this.id_token = id_token;
	}

	@JsonProperty("user_id")
	public String getUserID() {
		return user_id;
	}

	public void setUserID(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("display_name")
	public String getDisplayName() {
		return display_name;
	}

	public void setDisplayName(String display_name) {
		this.display_name = display_name;
	}

	@JsonProperty("given_name")
	public String getGivenName() {
		return given_name;
	}

	public void setGivenName(String given_name) {
		this.given_name = given_name;
	}

	@JsonProperty("family_name")
	public String getFamilyName() {
		return family_name;
	}

	public void setFamilyName(String family_name) {
		this.family_name = family_name;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@JsonProperty("picture_url")
	public String getPictureURL() {
		return picture_url;
	}

	public void setPictureURL(String picture_url) {
		this.picture_url = picture_url;
	}
}
