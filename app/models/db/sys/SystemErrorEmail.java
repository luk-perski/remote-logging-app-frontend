package models.db.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Model;

@Entity
@Table(name = "sys_system_error_email")
public class SystemErrorEmail extends Model {

	private static final int MAX_SIZE_FROM_ADDRESS = 100;
	private static final int MAX_SIZE_RECIPIENTS = 200;

	@Id
	private Long id;

	@Column(length = MAX_SIZE_FROM_ADDRESS, nullable = false)
	private String from_address;

	@Column(length = MAX_SIZE_RECIPIENTS, nullable = false)
	private String recipients;

	private String subject;

	@Column(columnDefinition = "TEXT")
	private String plain_message;

	@Column(columnDefinition = "TEXT")
	private String html_message;

	private Date date_buffered;

	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date date_error;

	@Column(length = 500)
	private String error_message;

	public SystemErrorEmail(SystemBufferedEmail buffered_email, String error_message) {
		this.subject = buffered_email.getSubject();
		this.recipients = buffered_email.getRecipients();
		this.from_address = buffered_email.getFromAddress();
		this.plain_message = buffered_email.getPlainMessage();
		this.html_message = buffered_email.getHTMLMessage();
		this.date_buffered = buffered_email.getDateBuffered();
		this.date_error = new Date();
		this.error_message = error_message;
	}

	public Long getID() {
		return id;
	}

	public void setRecipients(String recipients) {
		if (recipients == null || recipients.length() > MAX_SIZE_RECIPIENTS) {
			throw new IllegalArgumentException("Invalid Recipients");
		}
		this.recipients = recipients;
	}

	public void setFromAddress(String from_address) {
		if (from_address == null || from_address.length() > MAX_SIZE_FROM_ADDRESS) {
			throw new IllegalArgumentException("Invalid From address");
		}
		this.from_address = from_address;
	}

	public String getSubject() {
		return subject;
	}

	public String getPlainMessage() {
		return plain_message;
	}

	public String getHTMLMessage() {
		return html_message;
	}

	public Date getDateBuffered() {
		return date_buffered;
	}

	public Date getDateError() {
		return date_error;
	}

	public String getErrorMessage() {
		return error_message;
	}
}
