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
@Table(name = "sys_redirect_record")
public class RedirectRecord extends Model {

	private static final int MAX_SIZE_SLUG = 1000;

	@Id
	public Long id;

	@Column(length = MAX_SIZE_SLUG, nullable = false, unique = true)
	public String original_slug;

	@Column(length = MAX_SIZE_SLUG, nullable = false, unique = true)
	public String redirect_slug;

	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
	public Date creation_date;

	public Date last_edit_date;

	private static Finder<Long, RedirectRecord> finder = new Finder<Long, RedirectRecord>(RedirectRecord.class);

	public static RedirectRecord getByOriginalSlug(String original_slug) {
		return (original_slug == null) ? null : finder.query().where().eq("original_slug", original_slug).setMaxRows(1).findOne();
	}

	public static List<RedirectRecord> getByRedirectSlug(String redirect_slug) {
		return (redirect_slug == null) ? null : finder.query().where().eq("redirect_slug", redirect_slug).findList();
	}

	public static String getRedirectSlugByOriginalSlug(String original_slug) {
		RedirectRecord redirect_record = getByOriginalSlug(original_slug);
		return (redirect_record != null) ? redirect_record.redirect_slug : null;
	}

	public static void updateRedirectRecord(String original_slug, String redirect_slug) {
		if (original_slug != null && !original_slug.trim().isEmpty() && redirect_slug != null && !redirect_slug.trim().isEmpty() && !original_slug.equalsIgnoreCase(redirect_slug)) {
			// Update the redirect record
			RedirectRecord record = getByOriginalSlug(original_slug);
			if (record == null) { // If it doesn't exist, create it
				record = new RedirectRecord();
				record.creation_date = new Date();
				record.original_slug = original_slug;
			} else { // If it exists, update it
				record.last_edit_date = new Date();
			}
			record.redirect_slug = redirect_slug;
			record.save();
		}
	}
}
