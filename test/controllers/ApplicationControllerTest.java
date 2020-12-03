package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

import org.junit.Test;

import models.db.app.config.ApplicationConfiguration;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import utils.app.config.AppConfig;

public class ApplicationControllerTest extends WithApplication {

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Test
	public void testIndex() {
		Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri(controllers.routes.ApplicationController.index().toString());
		Result result = route(app, request);
		assertEquals(OK, result.status());
	}

	@Test
	public void testConfiguration() {
		ApplicationConfiguration configuration = AppConfig.getInstance();
		assertNotNull(configuration);
		assertTrue(configuration.isValid());
	}
}
