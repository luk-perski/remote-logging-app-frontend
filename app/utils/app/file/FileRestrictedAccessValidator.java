package utils.app.file;

import play.mvc.Http.Request;
import utils.Utils;

public interface FileRestrictedAccessValidator {

	public boolean checkRestrictedAccess(Request request, Utils utils, String validation_class, String validation_data);
}
