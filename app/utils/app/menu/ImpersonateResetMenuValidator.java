package utils.app.menu;

import models.db.user.User;
import play.mvc.Http.RequestHeader;
import utils.Utils;

public class ImpersonateResetMenuValidator implements ApplicationMenuCustomValidator {

	@Override
	public boolean passesCustomValidation(RequestHeader request, Utils utils) {

		if (utils.session_manager.isInAdminUserMode(request)) {
			// Get the admin user from session
			User admin_user = User.getByID(utils.type_utils.getLongValue(utils.session_manager.getAdminUser(request.session())));

			// If the admin user exists in the session, then this menu option can be shown
			return admin_user != null;
		}

		return false;
	}

}
