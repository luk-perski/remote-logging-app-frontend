package models.db.app.files;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Model;
import models.db.user.User;

@Entity
@Table(name = "app_files_resource_associated_file_access_record")
public class ResourceAssociatedFileAccessRecord extends Model {

	@Id
	private Long id;

	// The user that accessed the file
	@ManyToOne
	@Column(nullable = false)
	private User user;

	// The file that was accessed
	@ManyToOne
	@Column(nullable = false)
	private ResourceAssociatedFile resource_associated_file;

	// The date the user accessed this file
	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	private Date accessed_date;

	public ResourceAssociatedFileAccessRecord(User user, ResourceAssociatedFile resource_associated_file, Date accessed_date) {
		super();
		this.user = user;
		this.resource_associated_file = resource_associated_file;
		this.accessed_date = accessed_date;
	}

	public Long getID() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public ResourceAssociatedFile getResourceAssociatedFile() {
		return resource_associated_file;
	}

	public Date getAccessedDate() {
		return accessed_date;
	}
}
