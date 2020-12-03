package models.db.sys;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "sys_system_buffered_email")
public class SystemBufferedEmail extends Model {

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

	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date date_buffered;

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private Integer urgency;

	private static Finder<Long, SystemBufferedEmail> finder = new Finder<Long, SystemBufferedEmail>(SystemBufferedEmail.class);

	public static List<SystemBufferedEmail> getAll() {
		return finder.all();
	}

	public static List<SystemBufferedEmail> getAllOrderedByUrgencyWithLimit(int limit) {
		return finder.query().where().orderBy("urgency").setMaxRows(limit).findList();
	}

	public Long getID() {
		return id;
	}

	public String getFromAddress() {
		return from_address;
	}

	public String getRecipients() {
		return recipients;
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

	public int getUrgency() {
		return urgency;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public void setPlainMessage(String plain_message) {
		this.plain_message = plain_message;
	}

	public void setHTMLMessage(String html_message) {
		this.html_message = html_message;
	}

	public void setDateBuffered(Date date) {
		this.date_buffered = date;
	}

	public void setUrgency(int urgency) {
		this.urgency = urgency;
	}
}
