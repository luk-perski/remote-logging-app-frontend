package managers;

import java.time.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import models.db.app.config.ApplicationConfiguration;
import utils.app.config.AppConfig;

@Singleton
public class OnStartup {

	private static final Logger log = LoggerFactory.getLogger(OnStartup.class);

	private static final Duration JOBS_INITIAL_DELAY = Duration.ofSeconds(5);
	private static final Duration JOBS_INTERVAL = Duration.ofSeconds(15);

	private final ActorSystem actor_system;

	@Inject
	public OnStartup(ActorSystem actor_system) {

		this.actor_system = actor_system;

		log.info("Application starting...");
		// ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralAction("Application starting"));
		// ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralAction("Application started"));
	}

	private boolean loadApplicationConfiguration() {
		ApplicationConfiguration configuration = null;
		try {
			configuration = AppConfig.getInstance();
		} catch (Exception e) {
			log.error("Unable to retrieve aplication configuration instance: " + e.getMessage());
		}
		if (configuration == null || !configuration.isValid()) {
			log.error("Cannot find a valid application configuration! Application will most likely malfunction.");
			return false;
		}

		return true;
	}
}
