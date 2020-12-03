package utils.auth.oauth;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import models.db.app.config.ApplicationConfiguration;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSBodyWritables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http.Request;
import pt.iscte_iul.gdsi.utils.WebUtils;
import utils.app.config.AppConfig;
import utils.auth.oauth.models.AuthenticationStepResult;
import utils.auth.oauth.models.IdToken;
import utils.auth.oauth.models.OAuthUserInfo;
import utils.auth.oauth.models.TokenResponse;
import utils.auth.oauth.models.UserPhoto;
import utils.auth.oauth.models.UserProfile;

public class Office365OAuthHelper implements WSBodyReadables, WSBodyWritables {

	private static final Logger log = LoggerFactory.getLogger(Office365OAuthHelper.class);

	@Inject
	private Config config;

	@Inject
	private WSClient ws;

	@Inject
	private WebUtils web_utils;

	public Office365OAuthHelper() {
	}

	public String buildOAuthFirstStepURL(Request request, String nonce, String redirect) {

		// Get the application configuration
		ApplicationConfiguration app_config = AppConfig.getInstance();

		// Get the base URL for the OAuth service
		String url = config.getString("custom.office365.oauth.base_url");
		String tenant_id = config.getString("custom.office365.oauth.tenant_id");
		if (tenant_id != null) {
			url += tenant_id;
		}
		String authorize_url_suffix = config.getString("custom.office365.oauth.authorize_url_suffix");
		if (authorize_url_suffix != null) {
			url += authorize_url_suffix;
		}

		String redirect_uri = "";
		try {
			redirect_uri = this.web_utils.encodeURL(controllers.routes.AuthenticationController.processOffice365OAuthLoginCallback().absoluteURL(request, app_config.isProduction() || app_config.isQuality()).toString());
		} catch (UnsupportedEncodingException ignore) {
		}

		// Adding parameters for the OAuth request to the base URL
		url += "?client_id=" + config.getString("custom.office365.oauth.client_id");
		url += "&redirect_uri=" + redirect_uri;
		url += "&response_type=" + config.getString("custom.office365.oauth.response_type");
		url += "&response_mode=" + config.getString("custom.office365.oauth.response_mode");
		url += "&scope=" + config.getString("custom.office365.oauth.scope");
		if (redirect != null && !redirect.trim().isEmpty()) {
			url += "&state=" + redirect;
		} else {
			url += "&state=%2F";
		}
		url += "&nonce=" + nonce;

		log.trace("URL: " + url);

		return url;
	}

	private String buildOAuthAccessTokenRequestURL() {
		// Get the base URL for the OAuth service
		String url = config.getString("custom.office365.oauth.base_url");
		String tenant_id = config.getString("custom.office365.oauth.tenant_id");
		if (tenant_id != null) {
			url += tenant_id;
		}
		String token_url_suffix = config.getString("custom.office365.oauth.token_url_suffix");
		if (token_url_suffix != null) {
			url += token_url_suffix;
		}

		log.trace("URL: " + url);

		return url;
	}

	private String buildOAuthAccessTokenRequestBody(Request request, String code) {
		// Get the application configuration
		ApplicationConfiguration app_config = AppConfig.getInstance();

		String redirect_uri = "";
		String client_secret = "";
		try {
			redirect_uri = this.web_utils.encodeURL(controllers.routes.AuthenticationController.processOffice365OAuthLoginCallback().absoluteURL(request, app_config.isProduction() || app_config.isQuality()).toString());
			client_secret = this.web_utils.encodeURL(config.getString("custom.office365.oauth.client_secret"));
		} catch (UnsupportedEncodingException ignore) {
		}

		// Build the body content
		String body = "grant_type=authorization_code";
		body += "&client_id=" + config.getString("custom.office365.oauth.client_id");
		body += "&code=" + code;
		body += "&redirect_uri=" + redirect_uri;
		body += "&scope=" + config.getString("custom.office365.oauth.scope");
		body += "&client_secret=" + client_secret;

		log.trace("BODY: " + body);

		return body;
	}

	public CompletionStage<AuthenticationStepResult> processLoginCallback(Request request, String code, String id_token, String expected_nonce, String state, String session_state) {
		if (code != null && !code.trim().isEmpty() && id_token != null && !id_token.trim().isEmpty() && expected_nonce != null && !expected_nonce.trim().isEmpty() && state != null && !state.trim().isEmpty()) {
			AuthenticationStepResult step_result = retrieveOAuthUserInfo(code, id_token, expected_nonce, state);
			if (step_result != null) {
				if (step_result.isError()) {
					return CompletableFuture.supplyAsync(() -> step_result);
				}

				if (step_result.getResultData() != null && step_result.getResultData() instanceof OAuthUserInfo) {
					OAuthUserInfo oauth_user_info = (OAuthUserInfo) step_result.getResultData();
					if (oauth_user_info.getAuthorizationCode() != null && !oauth_user_info.getAuthorizationCode().trim().isEmpty()) {
						// We have an authorization code, let's get an access token

						String url = buildOAuthAccessTokenRequestURL();
						String post_body = buildOAuthAccessTokenRequestBody(request, code);

						CompletionStage<WSResponse> ws_result = ws.url(url).setContentType("application/x-www-form-urlencoded").post(post_body);
						CompletionStage<AuthenticationStepResult> access_token_response = ws_result.thenApplyAsync(ws_response -> processOAuthAccessTokenRequestResponse(request, ws_response, oauth_user_info));
						return access_token_response.thenCompose(token_step_result -> getOauthDataForUser(request, token_step_result));
					}
				}
			}
		}
		return CompletableFuture.supplyAsync(() -> new AuthenticationStepResult(true, "auth.error.invalid_data_received", null));
	}

	private AuthenticationStepResult processOAuthAccessTokenRequestResponse(Request request, WSResponse response, OAuthUserInfo oauth_user_info) {
		String response_body = response.getBody();
		log.trace("ACCESS TOKEN REQUEST RESPONSE BODY: " + response_body);

		if (response_body != null && !response_body.trim().isEmpty()) {
			TokenResponse token_response = Json.fromJson(Json.parse(response_body), TokenResponse.class);
			if (token_response != null) {
				if (token_response.getError() != null && !token_response.getError().trim().isEmpty()) { // It's an error
					return new AuthenticationStepResult(true, token_response.getError() + " - " + token_response.getErrorDescription(), null);
				} else { // It's not an error
					oauth_user_info.setAccessTokenType(token_response.getTokenType());
					oauth_user_info.setAccessToken(token_response.getAccessToken());
					return new AuthenticationStepResult(false, null, oauth_user_info);
				}
			}
		}

		return new AuthenticationStepResult(true, "auth.error.invalid_data_received", null);
	}

	private CompletionStage<AuthenticationStepResult> getOauthDataForUser(Request request, AuthenticationStepResult token_step_result) {
		if (token_step_result != null) {
			if (token_step_result.isError()) {
				return CompletableFuture.supplyAsync(() -> token_step_result);
			}

			if (token_step_result.getResultData() != null && token_step_result.getResultData() instanceof OAuthUserInfo) {
				OAuthUserInfo user_info = (OAuthUserInfo) token_step_result.getResultData();

				String url = config.getString("custom.office365.oauth.api_url");
				url += "/users/" + user_info.getUserID();

				log.trace("USER DATA REQ: " + url);

				WSRequest req = ws.url(url).addHeader("Authorization", user_info.getAccessTokenType() + " " + user_info.getAccessToken());

				return req.get().thenApplyAsync(ws_response -> processOAuthDataForUserResponse(request, ws_response, user_info));
			}
		}

		return CompletableFuture.supplyAsync(() -> new AuthenticationStepResult(true, "auth.error.invalid_data_received", null));
	}

	private AuthenticationStepResult processOAuthDataForUserResponse(Request request, WSResponse response, OAuthUserInfo user_info) {

		String response_body = response.getBody();
		log.trace("USER DATA RESPONSE BODY: " + response_body);

		UserProfile user_profile = Json.fromJson(Json.parse(response_body), UserProfile.class);
		if (user_profile != null) {
			user_info.setEmail(user_profile.getMail());
			user_info.setFamilyName(user_profile.getSurname());
			user_info.setGivenName(user_profile.getGivenName());
			user_info.setName(user_profile.getGivenName() + " " + user_profile.getSurname());
			user_info.setDisplayName(user_profile.getDisplayName());
			user_info.setLocale(user_profile.getPreferredLanguage());
		}

		return new AuthenticationStepResult(false, null, user_info);
	}

	public CompletionStage<UserPhoto> getUserPhoto(Request request, OAuthUserInfo user_info) {

		if (user_info != null) {
			String url = config.getString("custom.office365.oauth.api_url");
			url += "/users/" + user_info.getUserID() + "/photo/$value";

			log.trace("USER PHOTO REQ: " + url);

			WSRequest req = ws.url(url).addHeader("Authorization", user_info.getAccessTokenType() + " " + user_info.getAccessToken());
			return req.get().thenApplyAsync(ws_response -> processGetUserPhotoResponse(request, ws_response));
		}

		return CompletableFuture.supplyAsync(() -> null);
	}

	private UserPhoto processGetUserPhotoResponse(Request request, WSResponse response) {
		if (response != null) {
			byte[] image_bytes = response.asByteArray();
			String content_type = response.getSingleHeader("Content-Type").get();
			return new UserPhoto(image_bytes, content_type);
		}
		return null;
	}

	private AuthenticationStepResult retrieveOAuthUserInfo(String code, String id_token_string, String expected_nonce, String state) {
		log.trace("Retrieving OAuth user info...");
		if (id_token_string != null && expected_nonce != null) {
			IdToken id_token = IdToken.parseEncodedToken(id_token_string, expected_nonce);
			if (id_token != null) {
				String email = id_token.getEmail();
				String username = id_token.getPreferredUsername();
				String name = id_token.getName();
				long expires_in = id_token.getExpirationTime();
				String user_id = id_token.getObjectId();
				String tenant_id = id_token.getTenantId();
				long not_before = id_token.getNotBefore();
				String nonce = id_token.getNonce();

				if (nonce == null || !nonce.equalsIgnoreCase(expected_nonce)) {
					return new AuthenticationStepResult(true, "auth.error.nonce_does_not_match", null);
				}

				log.trace("ID_TOKEN: " + id_token_string);
				log.trace("EXPIRES: " + expires_in);
				log.trace("USER ID: " + user_id);
				log.trace("EMAIL: " + email);
				log.trace("NAME: " + name);
				log.trace("USERNAME: " + username);
				log.trace("TENANT ID: " + tenant_id);
				log.trace("NOT BEFORE: " + not_before);
				log.trace("NONCE: " + nonce);
				log.trace("STATE: " + state);

				OAuthUserInfo user_info = new OAuthUserInfo();
				user_info.setService("Office365");
				user_info.setAuthorizationCode(code);
				user_info.setState(state);
				user_info.setExpiresIn(new Long(expires_in).intValue());
				user_info.setIDToken(id_token_string);
				user_info.setUserID(user_id);
				user_info.setUsername(username);
				user_info.setEmail((email != null) ? email : username);
				user_info.setName(name);

				return new AuthenticationStepResult(false, null, user_info);
			}
		}

		return new AuthenticationStepResult(true, "auth.error.invalid_data_received", null);
	}
}
