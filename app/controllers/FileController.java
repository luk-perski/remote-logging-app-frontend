package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.Assets.Asset;
import models.db.app.files.ResourceAssociatedFile;
import models.db.app.files.ResourceAssociatedFileAccessRecord;
import models.db.user.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.Utils;
import utils.app.file.FileRestrictedAccessValidator;
import views.html.base.basic_error_page;

public class FileController extends Controller {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@Inject
	private Utils utils;

	public Result renderDefaultUserPhoto(Request request) {
		return redirect(routes.Assets.versioned(new Asset("images/default_user.png")));
	}

	public Result renderResourceAssociatedFile(Request request, Long id) {
		try {
			User auth_user = User.getByID(this.utils.type_utils.getLongValue(this.utils.session_manager.getAuthenticatedUserID(request)));

			ResourceAssociatedFile resource_file = ResourceAssociatedFile.getByID(id);
			if (resource_file == null) {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.record_not_found"), false));
			}

			// Check if the record has restricted access and if this request's user can access it
			if (!resourceIsAccessible(request, resource_file)) {
				// If not, show forbidden error
				return forbidden(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.forbidden"), this.utils.l.l(request, "error.text.forbidden"), false));
			}

			// If access record audit is activated for this resource file
			if (resource_file.recordAccesses()) {
				// create a file access record
				new ResourceAssociatedFileAccessRecord(auth_user, resource_file, new Date()).save();
			}

			return renderFile(request, resource_file);

		} catch (Exception e) {
			log.error("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.record_not_found"), false));

	}

	private boolean resourceIsAccessible(Request request, ResourceAssociatedFile resource_file) {
		if (!resource_file.isPublic()) {
			if (resource_file.getRestrictedAccessValidationClass() != null) {
				try {
					FileRestrictedAccessValidator validator = (FileRestrictedAccessValidator) Class.forName(resource_file.getRestrictedAccessValidationClass()).newInstance();
					if (validator != null) {
						return validator.checkRestrictedAccess(request, this.utils, resource_file.getRestrictedAccessValidationClass(), resource_file.getRestrictedAccessValidationData());
					}
				} catch (ClassNotFoundException ignore) {
					ignore.printStackTrace();
				} catch (InstantiationException ignore) {
					ignore.printStackTrace();
				} catch (IllegalAccessException ignore) {
					ignore.printStackTrace();
				}
			}
			return false;
		}
		return true;
	}

	private Result renderFile(Request request, ResourceAssociatedFile resource_file) {
		File file = this.utils.static_content_utils.getFile(resource_file.getFilePath());
		try {
			String real_file_hash = this.utils.other_utils.getFileMD5Hash(file);
			if (real_file_hash != null && resource_file.getFileHash() != null && real_file_hash.equals(resource_file.getFileHash())) {
				InputStream stream = this.utils.static_content_utils.getFileAsInputStream(resource_file.getFilePath());
				if (stream != null) {
					Result response = ok(stream);
					response = response.withHeader(Http.HeaderNames.CACHE_CONTROL, "public");
					response = response.withHeader(Http.HeaderNames.CONTENT_DISPOSITION, "filename=" + resource_file.getFileName());
					response = response.withHeader(Http.HeaderNames.CONTENT_TYPE, resource_file.getFileContentType());
					return response.as(resource_file.getFileContentType());
				}
			} else {
				return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "error.text.file_hash_integrity"), false));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return internalServerError(basic_error_page.render(request, this.utils, null, this.utils.l.l(request, "error.label.unexpected_error"), this.utils.l.l(request, "general.text.record_not_found"), false));
	}
}
