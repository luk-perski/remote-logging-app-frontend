package managers.jobs;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ebean.Ebean;
import models.db.app.config.ApplicationConfiguration;
import play.libs.mailer.MailerClient;
import utils.app.config.AppConfig;
import utils.mail.MailHelper;

public class CheckEmailBufferJob {

	private static final Logger log = LoggerFactory.getLogger(CheckEmailBufferJob.class);

	@Inject
	private MailHelper mail_helper;

	@Inject
	private MailerClient mailer_client;

	public String runEmailBufferCheck() {
		String out = "";

		log.info("Checking system e-mail buffer...");
		out += "Checking system e-mail buffer...\n";

		try {
			Ebean.beginTransaction();

			out += this.mail_helper.runBufferCheck(mailer_client);

			Ebean.commitTransaction();
		} catch (Exception e) {
			sendErrorEmail("Exception (Maintenance Task - CHECK EMAIL BUFFER)", "Exception (Maintenance Task - CHECK EMAIL BUFFER): " + e.getMessage());
			log.error("Exception (Maintenance Task - CHECK EMAIL BUFFER): " + e.getMessage());
			out += "ERROR: Exception (Maintenance Task - CHECK EMAIL BUFFER): " + e.getMessage() + "\n";
			e.printStackTrace();
		} finally {
			Ebean.endTransaction();
		}

		log.info("Done.");
		out += "Done.\n";

		return out;
	}

	private void sendErrorEmail(String subject, String message) {
		try {
			ApplicationConfiguration app_config = AppConfig.getInstance();
			if (app_config != null) {
				String _subject = "[" + app_config.getApplicationNamePT() + "] " + subject;
				String _from = app_config.getApplicationNamePT() + " (Scheduled Jobs) <no-reply@" + app_config.getBaseDomain() + ">";
				this.mail_helper.bufferMail(_subject, "support@" + app_config.getBaseDomain(), _from, message, null, MailHelper.URGENCY_HIGH);
			} else {
				log.error("Unable to retrieve application configuration!");
			}
		} catch (Exception e) {
			log.error("Unable to buffer email message [Error: " + e.getMessage() + "]");
		}
	}

}
