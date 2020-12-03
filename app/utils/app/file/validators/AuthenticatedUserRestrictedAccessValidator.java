package utils.app.file.validators;

import com.fasterxml.jackson.annotation.JsonProperty;

import play.libs.Json;
import play.mvc.Http.Request;
import utils.Utils;
import utils.app.file.FileRestrictedAccessValidator;

public class AuthenticatedUserRestrictedAccessValidator implements FileRestrictedAccessValidator {

	private long user_id_with_access;

	@Override
	public boolean checkRestrictedAccess(Request request, Utils utils, String validation_class, String validation_data) {
		if (utils.session_manager.isAuthenticated(request)) {
			Long auth_user_id = utils.type_utils.getLongValue(utils.session_manager.getAuthenticatedUserID(request));
			if (auth_user_id != null) {
				try {
					Class<?> validation_clazz = Class.forName(validation_class);
					if (validation_clazz != null && validation_data != null) {
						Object object_from_json = Json.fromJson(Json.parse(validation_data), validation_clazz);
						if (object_from_json != null && object_from_json instanceof AuthenticatedUserRestrictedAccessValidator) {
							AuthenticatedUserRestrictedAccessValidator validator = (AuthenticatedUserRestrictedAccessValidator) object_from_json;
							return validator.user_id_with_access == auth_user_id.longValue();
						}
					}
				} catch (ClassNotFoundException warning) {
					warning.printStackTrace();
				}
			}
		}
		return false;
	}

	public AuthenticatedUserRestrictedAccessValidator() {
	}

	public AuthenticatedUserRestrictedAccessValidator(long user_id_with_access) {
		this.user_id_with_access = user_id_with_access;
	}

	@JsonProperty("user_id_with_access")
	public long getUserIDWithAccess() {
		return this.user_id_with_access;
	}

	public void setUserIDWithAccess(long user_id_with_access) {
		this.user_id_with_access = user_id_with_access;
	}
}
