package utils.app.file.validators;

import play.mvc.Http.Request;
import utils.Utils;
import utils.app.file.FileRestrictedAccessValidator;

public class AuthenticatedUsersValidator implements FileRestrictedAccessValidator {

	@Override
	public boolean checkRestrictedAccess(Request request, Utils utils, String validation_class, String validation_data) {
		return utils.session_manager.isAuthenticated(request);
	}
}
