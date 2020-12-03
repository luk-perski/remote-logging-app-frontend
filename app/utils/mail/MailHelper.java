package utils.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.db.sys.SystemBufferedEmail;
import models.db.sys.SystemErrorEmail;
import models.db.sys.SystemSentEmail;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import pt.iscte_iul.gdsi.utils.ValidationUtils;
import utils.mail.exception.MailHelperException;

@Singleton
public class MailHelper {

	private static final Logger log = LoggerFactory.getLogger(MailHelper.class);

	private static final int MAX_EMAILS_PER_CHECK = 5;

	public static final int URGENCY_LOW = 3;
	public static final int URGENCY_MEDIUM = 2;
	public static final int URGENCY_HIGH = 1;

	@Inject
	private ValidationUtils validation_utils;

	public void bufferMail(String subject, String recipients, String from_address, String plain_message, String html_message, int urgency) throws MailHelperException {
		if (from_address != null && !from_address.trim().isEmpty() && recipients != null && !recipients.trim().isEmpty()) {
			SystemBufferedEmail record = new SystemBufferedEmail();
			record.setSubject(subject);
			record.setRecipients(recipients);
			record.setFromAddress(from_address);
			record.setPlainMessage(plain_message);
			record.setHTMLMessage(html_message);
			record.setDateBuffered(new Date());
			record.setUrgency(urgency);
			record.save();
		} else {
			throw new MailHelperException("invalid message parameters (from address or recipients)");
		}
	}

	public void bufferMail(String subject, List<String> recipients, String from_address, String plain_message, String html_message, int urgency) throws MailHelperException {
		if (from_address != null && !from_address.trim().isEmpty() && recipients != null && !recipients.isEmpty()) {
			String _recipients = "";
			for (int i = 0; i < recipients.size(); i++) {
				if (recipients.get(0) != null && !recipients.get(0).trim().isEmpty()) {
					if (i > 0) {
						_recipients += ", ";
					}
					_recipients += recipients.get(0);
				}
			}
			if (!_recipients.trim().isEmpty()) {
				bufferMail(subject, _recipients, from_address, plain_message, html_message, urgency);
			} else {
				throw new MailHelperException("invalid recipients!");
			}
		} else {
			throw new MailHelperException("invalid message parameters (from address or recipients)");
		}
	}

	public String runBufferCheck(MailerClient mailer_client) throws PersistenceException {
		String out = "";

		List<SystemBufferedEmail> list = SystemBufferedEmail.getAllOrderedByUrgencyWithLimit(MAX_EMAILS_PER_CHECK);
		if (list != null && !list.isEmpty()) {
			log.debug("Buffered e-mails to process on this check: " + list.size());
			out += "Buffered e-mails to process on this check: " + list.size() + "\n";

			for (SystemBufferedEmail buffered_email : list) {
				sendBufferedEmail(mailer_client, buffered_email);
				buffered_email.delete();
			}
		} else {
			log.debug("No buffered e-mails. Nothing to do.");
			out += "No buffered e-mails. Nothing to do.\n";
		}

		return out;
	}

	private void sendBufferedEmail(MailerClient mailer_client, SystemBufferedEmail buffered_email) {
		try {
			// Check if recipients field is valid
			if (buffered_email.getRecipients() != null && !buffered_email.getRecipients().trim().isEmpty()) {
				List<String> recipients = new ArrayList<String>();

				// Check if multiple recipients
				if (buffered_email.getRecipients().indexOf(',') > -1) {
					for (String address : buffered_email.getRecipients().split(",")) {
						if (this.validation_utils.isValidEmail(address.trim())) {
							recipients.add(address.trim());
						}
					}
				} else { // Just one recipient
					if (this.validation_utils.isValidEmail(buffered_email.getRecipients().trim())) {
						recipients.add(buffered_email.getRecipients().trim());
					}
				}

				String sent_message_id = sendMail(mailer_client, buffered_email.getSubject(), recipients, buffered_email.getFromAddress(), buffered_email.getPlainMessage(), buffered_email.getHTMLMessage());

				createEmailSentRecord(buffered_email, sent_message_id);
			} else {
				createEmailErrorRecord(buffered_email, "invalid recipients!");
			}
		} catch (Exception e) {
			createEmailErrorRecord(buffered_email, e.getMessage());
		}
	}

	private String sendMail(MailerClient mailer_client, String subject, List<String> recipients, String from_address, String plain_message, String html_message) throws MailHelperException {
		if (mailer_client == null) {
			throw new MailHelperException("Configuration problem: null mailer client!");
		}

		if (subject == null || subject.trim().isEmpty()) {
			throw new MailHelperException("Message problem: empty subject!");
		}

		if (from_address == null || from_address.trim().isEmpty()) {
			throw new MailHelperException("Message problem: empty From Address!");
		}

		if (recipients == null || recipients.isEmpty()) {
			throw new MailHelperException("Message problem: empty recipients!");
		}

		if ((plain_message == null || plain_message.trim().isEmpty()) && (html_message == null || html_message.trim().isEmpty())) {
			throw new MailHelperException("Message problem: empty message!");
		}

		Email email = new Email();
		email.setCharset("utf-8");
		email.setFrom(from_address);
		email.setSubject(subject);
		email.setTo(recipients);

		if (plain_message != null && !plain_message.trim().isEmpty()) {
			email.setBodyText(plain_message);
		} else {
			email.setBodyHtml(html_message);
		}

		return mailer_client.send(email);
	}

	private void createEmailSentRecord(SystemBufferedEmail buffered_email, String sent_message_id) {
		new SystemSentEmail(buffered_email, sent_message_id).save();
	}

	private void createEmailErrorRecord(SystemBufferedEmail buffered_email, String error_message) {
		new SystemErrorEmail(buffered_email, error_message).save();
	}

}
