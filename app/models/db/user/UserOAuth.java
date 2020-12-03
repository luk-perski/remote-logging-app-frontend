package models.db.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;
import play.libs.Json;
import utils.auth.oauth.models.OAuthUserInfo;

@Entity
@Table(name = "user_user_oauth")
public class UserOAuth extends Model {

	private static final int MAX_SIZE_ID = 50;
	private static final int MAX_SIZE_DATA_PROCESSOR = 250;

	@Id
	@Column(length = MAX_SIZE_ID)
	private String id;

	@ManyToOne
	@Column(unique = true)
	private User user;

	private Date last_updated;

	@Column(length = MAX_SIZE_DATA_PROCESSOR)
	private String data_processor;

	@Column(columnDefinition = "TEXT")
	private String data;

	private static final Finder<Long, UserOAuth> finder = new Finder<Long, UserOAuth>(UserOAuth.class);

	public static UserOAuth getByUserID(Long id) {
		return (id == null) ? null : finder.query().where().eq("user.id", id).setMaxRows(1).findOne();
	}

	public static void updateDataByUser(User user, OAuthUserInfo data) {
		if (user != null) {
			UserOAuth record = getByUserID(user.getID());
			if (record == null) {
				record = new UserOAuth();
				record.setID(data.getUserID());
				record.setUser(user);
			}
			record.setLastUpdated(new Date());

			if (data != null) {
				record.setDataProcessor(OAuthUserInfo.class.getCanonicalName());
				record.setData(Json.stringify(Json.toJson(data)));
			}
			record.save();
		}
	}

	private void setUser(User user) {
		this.user = user;
	}

	private void setID(String user_id) {
		this.id = user_id;
	}

	private void setLastUpdated(Date date) {
		this.last_updated = date;
	}

	public Date getLastUpdated() {
		return this.last_updated;
	}

	private void setData(String data) {
		this.data = data;
	}

	private void setDataProcessor(String data_processor) {
		this.data_processor = data_processor;
	}

	public OAuthUserInfo getData() {
		if (this.data != null && this.data_processor != null) {
			try {
				return (OAuthUserInfo) Json.fromJson(Json.parse(this.data), Class.forName(this.data_processor));
			} catch (ClassNotFoundException ignore) {
			} catch (ClassCastException ignore) {
			}
		}

		return null;
	}
}
