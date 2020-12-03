package filters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.stream.Materializer;
import controllers.AuthenticationController;
import models.db.app.config.ApplicationConfiguration;
import models.db.user.Role;
import play.mvc.Controller;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import utils.Utils;
import utils.app.config.AppConfig;
import utils.app.page.PageSettings;
import utils.session.exception.InvalidRoleDataException;
import views.html.base.maintenance;

public class MaintenanceLockFilter extends Filter {

	private static final Logger log = LoggerFactory.getLogger(MaintenanceLockFilter.class);

	@Inject
	private Utils utils;

	@Inject
	public MaintenanceLockFilter(Materializer materializer) {
		super(materializer);
	}

	@Override
	public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next_filter, Http.RequestHeader request_header) {
		// Assets are still available in maintenance mode
		if (!request_header.path().startsWith("/assets/")) {
			boolean is_admin = false;
			try {
				is_admin = this.utils.session_manager.isAuthenticatedAs(AuthenticationController.ROLE_KEY, Role.ADMIN, request_header.session());
			} catch (InvalidRoleDataException warning) {
				log.warn(warning.getMessage());
			}

			// System is still available for people logged in as Admin
			if (!is_admin) {
				ApplicationConfiguration configuration = AppConfig.getInstance();
				if (configuration != null) {
					// Get the property value for the maintenance mode
					String property_value = configuration.getPropertyValue("MAINTENANCE_MODE_ON");
					if (property_value != null) {
						boolean maintenance_mode_on = Boolean.parseBoolean(property_value);
						// If maintenance mode is on, show the corresponding page
						if (maintenance_mode_on) {
							PageSettings page_settings = this.utils.page_settings_helper.generatePageSettings(request_header, this.utils, null, "", false);
							return CompletableFuture.completedFuture(Controller.ok(maintenance.render(request_header, this.utils, page_settings, null)));
						}
					}
				}
			}
		}

		// If maintenance mode is off, carry on
		return next_filter.apply(request_header).thenApply(result -> {
			return result;
		});
	}
}
