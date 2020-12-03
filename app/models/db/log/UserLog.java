package models.db.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.PagedList;
import io.ebean.annotation.Index;
import models.db.user.User;
import play.libs.Json;
import utils.log.models.LogAction;

@Entity
@Table(name = "log_user_log")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class UserLog extends Model {

	private static final Logger log = LoggerFactory.getLogger(UserLog.class);

	private static final int MAX_SIZE_CLASS_NAME = 250;

	private static final String DEFAULT_ORDER_BY = "instant DESC, id DESC";

	public static final String INVALID_INFO = "--invalid info--";

	@Id
	private Long id;

	// The class that defines this log
	@Column(length = MAX_SIZE_CLASS_NAME, nullable = false)
	private String class_name;

	// The user associated with this log
	@ManyToOne
	@Column(nullable = false)
	private User user;

	// The time in which this log occurred
	@Index
	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date instant;

	// The JSON data for this log
	@Column(columnDefinition = "TEXT")
	private String data;

	private static Finder<Long, UserLog> finder = new Finder<Long, UserLog>(UserLog.class);

	public static PagedList<UserLog> getAllPagedList(int page_size, int page_index) {
		return finder.query().where().orderBy(DEFAULT_ORDER_BY).setFirstRow(page_index * page_size).setMaxRows(page_size).findPagedList();
	}

	public static PagedList<UserLog> getByUserPagedList(String user_id, int page_size, int page_index) {
		return (user_id == null) ? null : finder.query().where().eq("user.id", user_id).orderBy(DEFAULT_ORDER_BY).setFirstRow(page_index * page_size).setMaxRows(page_size).findPagedList();
	}

	private UserLog(User user, Date instant, Object action_object) {
		if (user == null || instant == null || action_object == null) {
			throw new IllegalArgumentException("NULL arguments");
		}

		String class_name = action_object.getClass().getCanonicalName();
		if (class_name != null) {
			if (class_name.length() > MAX_SIZE_CLASS_NAME) {
				this.class_name = class_name.substring(0, MAX_SIZE_CLASS_NAME);
			} else {
				this.class_name = class_name;
			}
		}
		this.user = user;
		this.instant = instant;
		this.data = Json.stringify(Json.toJson(action_object));
	}

	public static void log(User user, Date instant, Object action_object) {
		new UserLog(user, instant, action_object).save();
	}

	public String getDescription() {
		String description = getDescription(getLogActionInstance());
		if (description != null && !description.trim().isEmpty()) {
			return description;
		}
		return INVALID_INFO;
	}

	public String getTypeLabel() {
		String type_label = getTypeLabel(getLogActionInstance());
		if (type_label != null && !type_label.trim().isEmpty()) {
			return type_label;
		}
		return INVALID_INFO;
	}

	private LogAction getLogActionInstance() {
		if (this.class_name != null && !this.class_name.trim().isEmpty() && this.data != null && !this.data.trim().isEmpty()) {
			try {
				Class<?> instance_class = Class.forName(this.class_name);
				return (LogAction) Json.fromJson(Json.parse(this.data), instance_class);
			} catch (ClassNotFoundException e) {
				log.error(e.getMessage());
			} catch (ClassCastException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	private String getDescription(LogAction instance) {
		return ((LogAction) instance).getDescription();
	}

	private String getTypeLabel(LogAction instance) {
		return ((LogAction) instance).getTypeLabel();
	}

	public static int getAllCount() {
		return finder.query().findCount();
	}

	public Date getInstant() {
		return this.instant;
	}

	public User getUser() {
		return this.user;
	}
}