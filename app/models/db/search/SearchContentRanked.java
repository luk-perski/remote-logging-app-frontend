package models.db.search;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@SuppressWarnings("unused")
@Entity
@Table(name = "search_search_content_ranked")
public class SearchContentRanked extends Model {

	@Id
	private Long id;

	private String search;

	@ManyToOne
	private SearchContentPrepared content_prepared;

	private Long hits = 0L;

	private static Finder<Long, SearchContentRanked> finder = new Finder<Long, SearchContentRanked>(SearchContentRanked.class);

	public static SearchContentRanked getSearchAndPrepared(String search_term, SearchContentPrepared prepared) {
		return finder.query().where().eq("search", search_term).eq("prepared", prepared).findOne();
	}
}
