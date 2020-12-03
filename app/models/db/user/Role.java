package models.db.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "user_role")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class Role extends Model {

	public static final int USER_ID = 1;
	public static final int ADMIN_ID = 2;
	public static final int MANAGER_ID = 3;
	public static final int STAFF_ID = 4;

	public static final String USER = "" + USER_ID;
	public static final String ADMIN = "" + ADMIN_ID;
	public static final String MANAGER = "" + MANAGER_ID;
	public static final String STAFF = "" + STAFF_ID;

	private static final int MAX_SIZE_ROLE_LABEL = 100;
	private static final int MAX_SIZE_ROLE_SHORT_LABEL = 10;

	@Id
	private Integer id;

	@Column(length = MAX_SIZE_ROLE_LABEL, nullable = false, unique = true)
	private String label_pt;

	@Column(length = MAX_SIZE_ROLE_LABEL, nullable = false, unique = true)
	private String label_en;

	@Column(length = MAX_SIZE_ROLE_SHORT_LABEL, nullable = false, unique = true)
	private String short_label_pt;

	@Column(length = MAX_SIZE_ROLE_SHORT_LABEL, nullable = false, unique = true)
	private String short_label_en;

	private static final Finder<Integer, Role> finder = new Finder<Integer, Role>(Role.class);

	public static Role getByID(Integer id) {
		return (id == null) ? null : finder.byId(id);
	}

	public static List<Role> getAll() {
		return finder.all();
	}

	public Integer getID() {
		return this.id;
	}

	@JsonProperty("label_pt")
	public String getLabelPT() {
		return label_pt;
	}

	public void setLabelPT(String label_pt) {
		this.label_pt = label_pt;
	}

	@JsonProperty("label_en")
	public String getLabelEN() {
		return label_en;
	}

	public void setLabelEN(String label_en) {
		this.label_en = label_en;
	}

	@JsonProperty("short_label_pt")
	public String getShortLabelPT() {
		return short_label_pt;
	}

	public void setShortLabelPT(String short_label_pt) {
		this.short_label_pt = short_label_pt;
	}

	@JsonProperty("short_label_en")
	public String getShortLabelEN() {
		return short_label_en;
	}

	public void setShortLabelEN(String short_label_en) {
		this.short_label_en = short_label_en;
	}
}
