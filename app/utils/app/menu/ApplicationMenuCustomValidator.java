package utils.app.menu;

import play.mvc.Http.RequestHeader;
import utils.Utils;

public interface ApplicationMenuCustomValidator {

	public boolean passesCustomValidation(RequestHeader request, Utils utils);
}
