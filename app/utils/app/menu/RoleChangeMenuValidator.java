package utils.app.menu;

import models.db.user.User;
import play.mvc.Http.RequestHeader;
import utils.Utils;

public class RoleChangeMenuValidator implements ApplicationMenuCustomValidator {

	@Override
	public boolean passesCustomValidation(RequestHeader request, Utils utils) {
		// Check if user is authenticated
		if (!utils.session_manager.isAuthenticated(request)) {
			return false;
		}

		// Check if the user record can be retrieved
		User user = User.getByID(utils.type_utils.getLongValue(utils.session_manager.getAuthenticatedUserID(request)));
		if (user == null) {
			return false;
		}

		// Check if user can have multiple roles
		return user.hasMultipleRoles();
	}

}
