package models.db.search;

import javax.persistence.Entity;
import javax.persistence.Transient;

import io.ebean.annotation.Sql;

@Entity
@Sql
public class SearchContentResult {
	@Transient
	private Long id;

	@Transient
	private Long contentId;

	@Transient
	private String contentClass;

	@Transient
	private String title;

	@Transient
	private String slug;

	@Transient
	private String s1;

	@Transient
	private String s2;

	@Transient
	private String priority1;

	@Transient
	private String priority2;

	@Transient
	private String priority;

	public Long getID() {
		return id;
	}

	public Long getContentID() {
		return contentId;
	}

	public String getContentClass() {
		return contentClass;
	}

	public String getTitle() {
		return title;
	}

	public String getSlug() {
		return slug;
	}

	public String getS1() {
		return s1;
	}

	public String getS2() {
		return s2;
	}

	public String getPriority1() {
		return priority1;
	}

	public String getPriority2() {
		return priority2;
	}

	public String getPriority() {
		return priority;
	}
}
