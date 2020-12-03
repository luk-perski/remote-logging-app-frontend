package models.db.app.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "app_files_resource_associated_file_type")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class ResourceAssociatedFileType extends Model {

	public static final int USER_PHOTO = 1;
	public static final int REPORT = 2;

	@Id
	private Integer id;

	@Column(nullable = false)
	private String label_pt;

	@Column(nullable = false)
	private String label_en;

	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_visible;

	private static final Finder<Integer, ResourceAssociatedFileType> finder = new Finder<Integer, ResourceAssociatedFileType>(ResourceAssociatedFileType.class);

	public static ResourceAssociatedFileType getByID(Integer id) {
		return (id == null) ? null : finder.byId(id);
	}

	public ResourceAssociatedFileType(String label_pt, String label_en, Boolean is_visible) {
		this.label_pt = label_pt;
		this.label_en = label_en;
		this.is_visible = is_visible;
	}

	public Integer getID() {
		return this.id;
	}

	public String getLabelPT() {
		return this.label_pt;
	}

	public String getLabelEN() {
		return this.label_en;
	}

	public boolean isVisible() {
		return this.is_visible != null && this.is_visible;
	}
}
