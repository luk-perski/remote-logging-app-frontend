package utils.app.config;

import javax.inject.Singleton;

import models.db.app.config.ApplicationConfiguration;

@Singleton
public class AppConfig {

	private static ApplicationConfiguration APPLICATION_CONFIGURATION_INSTANCE = null;

	private AppConfig() {
	}

	public static ApplicationConfiguration getInstance() {
		if (APPLICATION_CONFIGURATION_INSTANCE == null) {
			APPLICATION_CONFIGURATION_INSTANCE = ApplicationConfiguration.getConfiguration();
		}
		return APPLICATION_CONFIGURATION_INSTANCE;
	}
}
