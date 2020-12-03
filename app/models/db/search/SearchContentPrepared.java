package models.db.search;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@SuppressWarnings("unused")
@Entity
@Table(name = "search_search_content_prepared")
public class SearchContentPrepared extends Model {

	private static final int MAX_SIZE_CONTENT_ID = 20;
	private static final int MAX_SIZE_TITLE = 2000;
	protected static final int MAX_SIZE_CONTENT_CLASS = 200;
	private static final int MAX_SIZE_SLUG = 200;

	@Id
	private Long id;

	@Column(length = MAX_SIZE_CONTENT_ID, nullable = false)
	private String content_id;

	@Column(length = MAX_SIZE_CONTENT_CLASS, nullable = false)
	private String content_class;

	@Column(length = MAX_SIZE_TITLE, nullable = false)
	private String title;

	@Column(length = MAX_SIZE_SLUG, nullable = false)
	private String slug;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "content_prepared")
	private List<SearchContentRanked> ranked_list;

	@Column(nullable = false)
	private Boolean published = false;

	private Date publication_start;

	private Date publication_end;

	private Date submitted;

	@Column(columnDefinition = "MEDIUMTEXT")
	private String s1;

	@Column(columnDefinition = "TEXT")
	private String s2;

	private static Finder<Long, SearchContentPrepared> finder = new Finder<Long, SearchContentPrepared>(SearchContentPrepared.class);

	public static SearchContentPrepared byId(Long id) {
		return finder.byId(id);
	}

	public static SearchContentPrepared getOfType(String content_class, Long content_id) {
		return finder.query().where().eq("content_class", content_class).eq("content_id", content_id).findOne();
	}

	public SearchContentPrepared(String content_id, String content_class, String title, String slug, Boolean published, Date publication_start, Date publication_end, String s1, String s2) {
		this.content_id = content_id;
		this.content_class = content_class;
		this.title = title;
		this.slug = slug;
		this.published = published;
		this.publication_start = publication_start;
		this.publication_end = publication_end;
		this.s1 = s1;
		this.s2 = s2;
		this.submitted = new Date();
	}
}
