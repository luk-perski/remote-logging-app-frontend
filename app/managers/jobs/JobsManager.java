package managers.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedAbstractActor;
import models.db.sys.JobDescription;

public class JobsManager extends UntypedAbstractActor {

	private static final Logger log = LoggerFactory.getLogger(JobsManager.class);

	private JobsHelper jobs_helper;

	public JobsManager(JobsHelper jobs_helper) {
		this.jobs_helper = jobs_helper;
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		try {
			if (message instanceof JobsManagerProtocol.ProcessJobs) {
				if (JobDescription.areJobsRunning()) {
					log.warn("There are jobs still running. This shouldn't happen!");
				}
				List<JobDescription> jobs = JobDescription.getAllScheduled();
				if (jobs == null || jobs.isEmpty()) {
					log.info("No scheduled jobs. Yay, time for a nap!");
				} else {
					log.info("Scheduled jobs to perform: " + jobs.size());
					int counter = 1;
					for (JobDescription job : jobs) {
						log.info("=> Running job #" + counter + "...");
						this.jobs_helper.runJob(job);
						counter++;
					}
					log.info("All done.");
				}
			} else {
				log.warn("Received unknown message: " + message.getClass());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while processing message (" + message.getClass() + "): " + e.getMessage());
		}
	}
}
