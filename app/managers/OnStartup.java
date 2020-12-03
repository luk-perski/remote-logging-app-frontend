package managers;

import java.time.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import managers.jobs.JobsHelper;
import managers.jobs.JobsManager;
import managers.jobs.JobsManagerProtocol;
import models.db.app.config.ApplicationConfiguration;
import utils.app.config.AppConfig;

@Singleton
public class OnStartup {

	private static final Logger log = LoggerFactory.getLogger(OnStartup.class);

	private static final Duration JOBS_INITIAL_DELAY = Duration.ofSeconds(5);
	private static final Duration JOBS_INTERVAL = Duration.ofSeconds(15);

	private final ActorSystem actor_system;
	private final JobsHelper jobs_helper;

	@Inject
	public OnStartup(ActorSystem actor_system, JobsHelper jobs_helper) {

		this.actor_system = actor_system;
		this.jobs_helper = jobs_helper;

		log.info("Application starting...");
		// ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralAction("Application starting"));

		if (loadApplicationConfiguration()) {
			loadJobsManager();
		}

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

	private void loadJobsManager() {
		log.info("Setting up job scheduler...");

		// Instantiate the actor for the jobs manager
		ActorRef manager_actor = actor_system.actorOf(Props.create(JobsManager.class, this.jobs_helper));
		// Schedule message to process jobs (continuously)
		this.actor_system.scheduler().scheduleAtFixedRate(JOBS_INITIAL_DELAY, JOBS_INTERVAL, manager_actor, new JobsManagerProtocol.ProcessJobs(), this.actor_system.dispatcher(), manager_actor);

		log.info("...done");

		// ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralAction("Job scheduler was set up"));
	}
}
