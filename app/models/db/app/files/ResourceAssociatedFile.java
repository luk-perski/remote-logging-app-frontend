package models.db.app.files;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.annotation.Index;

/**
 * This class represents files that are associated with some resource in the application. If the resource class and ID properties are NULL, that means
 * that the file is simply a general file for the application that is not associated with a particular resource.
 * 
 * @author alsl
 *
 */
@Entity
@Table(name = "app_files_resource_associated_file")
@JsonIgnoreProperties({ "_ebean_intercept" })
public class ResourceAssociatedFile extends Model {

	private static final int MAX_SIZE_RESOURCE_ID = 20;
	private static final int MAX_SIZE_RESOURCE_CLASS = 250;
	private static final int MAX_SIZE_FILE_HASH = 32;
	private static final int MAX_SIZE_FILE_PATH = 1000;

	@Id
	private Long id;

	@Index
	@Column(length = MAX_SIZE_RESOURCE_ID)
	private String resource_id;

	@Column(length = MAX_SIZE_RESOURCE_CLASS)
	private String resource_class;

	@ManyToOne
	@Column(nullable = false)
	private ResourceAssociatedFileType file_type;

	@Column(nullable = false)
	private String file_name;

	@Column(length = MAX_SIZE_FILE_PATH, nullable = false)
	private String file_path;

	@Index
	@Column(nullable = false, length = MAX_SIZE_FILE_HASH)
	private String file_hash;

	@Column(nullable = false)
	private Long file_size;

	private String file_content_type;

	// If the file is public, everyone can see it
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean is_public;

	// When the file is not public, these two properties should be used to manage the restricted access:
	// The Java class that manages the validation check (must implement the interface utils.app.file.FileRestrictedAccessValidator)
	private String restricted_access_validation_class;

	// The JSON data that supports the restricted access rules for the validation class
	@Column(columnDefinition = "TEXT")
	private String restricted_access_validation_data;

	// Whether the access to the file should be recorded
	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Boolean record_accesses;

	// The access records to this file (if record_accesses is true)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "resource_associated_file")
	private List<ResourceAssociatedFileAccessRecord> access_records;

	private static final Finder<Long, ResourceAssociatedFile> finder = new Finder<Long, ResourceAssociatedFile>(ResourceAssociatedFile.class);

	public ResourceAssociatedFile() {
	}

	public static ResourceAssociatedFile getByID(Long file_id) {
		return (file_id == null) ? null : finder.byId(file_id);
	}

	public static ResourceAssociatedFile getByHash(String file_hash) {
		return (file_hash == null) ? null : finder.query().where().eq("file_hash", file_hash).setMaxRows(1).findOne();
	}

	public static List<ResourceAssociatedFile> getByResource(String resource_class, String resource_id) {
		if (resource_class != null && resource_id != null) {
			return finder.query().where().eq("resource_class", resource_class).eq("resource_id", resource_id.toString()).findList();
		}
		return null;
	}

	public static List<ResourceAssociatedFile> getByResourceAndType(String resource_class, String resource_id, int file_type_id) {
		if (resource_class != null && resource_id != null) {
			return finder.query().where().eq("resource_class", resource_class).eq("resource_id", resource_id.toString()).eq("file_type.id", file_type_id).findList();
		}
		return null;
	}

	public boolean isPublic() {
		return this.is_public != null && this.is_public;
	}

	public Long getID() {
		return id;
	}

	public String getResourceID() {
		return resource_id;
	}

	public String getResourceClass() {
		return resource_class;
	}

	public ResourceAssociatedFileType getFileType() {
		return file_type;
	}

	public String getFileName() {
		return file_name;
	}

	public String getFilePath() {
		return file_path;
	}

	public String getFileHash() {
		return file_hash;
	}

	public Long getFileSize() {
		return file_size;
	}

	public String getFileContentType() {
		return file_content_type;
	}

	public String getRestrictedAccessValidationClass() {
		return this.restricted_access_validation_class;
	}

	public String getRestrictedAccessValidationData() {
		return this.restricted_access_validation_data;
	}

	public boolean recordAccesses() {
		return this.record_accesses != null && this.record_accesses;
	}

	public List<ResourceAssociatedFileAccessRecord> getAccessRecords() {
		return this.access_records;
	}

	public void setResourceID(String resource_id) {
		this.resource_id = resource_id;
	}

	public void setResourceClass(String resource_class) {
		this.resource_class = resource_class;
	}

	public void setFileType(ResourceAssociatedFileType file_type) {
		this.file_type = file_type;
	}

	public void setFileName(String file_name) {
		this.file_name = file_name;
	}

	public void setFilePath(String file_path) {
		this.file_path = file_path;
	}

	public void setFileHash(String file_hash) {
		this.file_hash = file_hash;
	}

	public void setFileSize(Long file_size) {
		this.file_size = file_size;
	}

	public void setFileContentType(String file_content_type) {
		this.file_content_type = file_content_type;
	}

	public void setIsPublic(Boolean is_public) {
		this.is_public = is_public;
	}

	public void setRestrictedAccessValidationClass(String restricted_access_validation_class) {
		this.restricted_access_validation_class = restricted_access_validation_class;
	}

	public void setRestrictedAccessValidationData(String restricted_access_validation_data) {
		this.restricted_access_validation_data = restricted_access_validation_data;
	}

	public void setRecordAccesses(Boolean record_accesses) {
		this.record_accesses = record_accesses;
	}
}
