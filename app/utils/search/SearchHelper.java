package utils.search;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Query;
import io.ebean.RawSqlBuilder;
import models.db.search.SearchContentResult;
import models.db.user.User;

@Singleton
public class SearchHelper {

	// private static final Logger log = LoggerFactory.getLogger(SearchHelper.class);

	private static final String PREPARED_TABLE = "search_search_content_prepared";
	private static final String RANKED_TABLE = "search_search_content_ranked";

	public PagedList<SearchContentResult> performGeneralSearch(String search_query, int page, int page_size) {

		String sql = buildGeneralSearchQuery(prepareSearchQuery(search_query));

		// log.trace(sql);

		RawSqlBuilder builder = RawSqlBuilder.parse(sql);
		Query<SearchContentResult> query = Ebean.find(SearchContentResult.class).setRawSql(builder.create());

		return query.setFirstRow((page - 1) * page_size).setMaxRows(page_size).findPagedList();
	}

	private String buildGeneralSearchQuery(String search_query) {
		String sql = "";
		sql += " SELECT priority1, priority2, pr1 + pr2 AS priority, id, content_id, title, content_class, slug, s1, s2 \n";
		sql += " FROM \n";
		sql += " 	( \n";
		sql += "		SELECT content_class, id, content_id, slug, title, s1, s2, priority1, priority2, priority1 / (SELECT AVG(hits) FROM " + RANKED_TABLE + ") AS pr1, 1 / priority2 AS pr2\n";
		sql += " 		FROM \n";
		sql += "			( \n";
		sql += "    			( \n";
		sql += " 					SELECT T2.*, @rn := @rn + 1 AS priority2 \n";
		sql += "        			FROM \n";
		sql += "						( \n";
		sql += " 							SELECT smp.content_class, smp.id, smp.content_id, smp.title, smp.s1, smp.s2, smp.slug, match(smp.title) against ('" + search_query + "' in boolean mode) AS scorePrimary, match(smp.s1, smp.s2, smp.slug) against ('" + search_query + "' in boolean mode) AS scoreSecondary, smr.hits AS priority1\n";
		sql += "            				FROM " + PREPARED_TABLE + " smp LEFT JOIN " + RANKED_TABLE + " AS smr on smr.content_prepared_id = smp.id \n";
		sql += "            				WHERE ( match(smp.title, smp.s1, smp.s2, smp.slug) against ('" + search_query + "' in boolean mode) ) \n";
		sql += "            				AND smr.search = '" + search_query + "' \n";
		sql += "            				AND smp.published = 1 \n";
		sql += "     						GROUP BY smp.content_class, smp.content_id, smp.id, smr.hits \n";
		sql += " 							ORDER BY ((scorePrimary * 45000 / (LENGTH(smp.title) * 1.5)) + scoreSecondary + (1 - IF(smp.publication_start is null, 0 , NOW()/10000000 - smp.publication_start/10000000))) DESC, smp.publication_start DESC \n";
		sql += " 						) AS T2, (SELECT @rn := 0) AS dummy \n";
		sql += "       			) \n";
		sql += "       			UNION \n";
		sql += "       			( \n";
		sql += " 					SELECT T2.*, @rn := @rn + 1 AS priority2 \n";
		sql += "        			FROM \n";
		sql += "           				( \n";
		sql += "							SELECT smp.content_class, smp.id, smp.content_id, smp.title, smp.s1, smp.s2, smp.slug, match(smp.title) against ('" + search_query + "' in boolean mode) AS scorePrimary, match(smp.s1, smp.s2, smp.slug) against ('" + search_query + "' in boolean mode) AS scoreSecondary, 0 AS priority1 \n";
		sql += "            				FROM " + PREPARED_TABLE + " smp \n";
		sql += "            				WHERE ( match(smp.title, smp.s1, smp.s2, smp.slug) against ('" + search_query + "' in boolean mode) ) \n";
		sql += " 							AND smp.published = 1 \n";
		sql += "               				AND smp.id not IN (SELECT content_prepared_id FROM " + RANKED_TABLE + " WHERE search = '" + search_query + "') \n";
		sql += "            				GROUP BY smp.content_class, smp.content_id, smp.id \n";
		sql += "            				ORDER BY ((scorePrimary * 45000 / (LENGTH(smp.title) * 1.5)) + scoreSecondary + (1 - IF(smp.publication_start is null, 0 , NOW()/10000000 - smp.publication_start/10000000))) DESC, smp.publication_start DESC \n";
		sql += " 						) AS T2, (SELECT @rn := 0) AS dummy \n";
		sql += "       			) \n";
		sql += "   			) AS T \n";
		sql += " 		ORDER BY T.priority1 DESC, T.priority2 ASC \n";
		sql += " 	) TPR\n";
		sql += " ORDER BY priority DESC \n";
		return sql;
	}

	public PagedList<User> performUserSearch(String search_query, int page, int page_size) {
		ExpressionList<User> query = Ebean.find(User.class).where();
		String[] terms = search_query.split(" ");

		query = query.and();
		for (String term : terms) {
			query = query.or();
			query = query.ilike("name", "%" + term + "%");
			query = query.ilike("username", "%" + term + "%");
			query = query.ilike("email", "%" + term + "%");
			query = query.ilike("roles.role.label_pt", "%" + term + "%");
			query = query.ilike("roles.role.label_en", "%" + term + "%");
			query = query.endOr();
		}
		query = query.endAnd();

		return query.setFirstRow((page - 1) * page_size).setMaxRows(page_size).findPagedList();
	}

	private static String prepareSearchQuery(String search_query) {
		// substitute chars that may cause a problem with the boolean search
		search_query = search_query.replaceAll("\\*-", "*");
		search_query = search_query.replaceAll("-\\*", "*");
		search_query = search_query.replaceAll("\\\\", "\\\\\\\\");
		search_query = search_query.replaceAll("'", "\\\\'");
		search_query = search_query.replaceAll("\\n", "\\\\n");
		search_query = search_query.replaceAll("\\r", "\\\\r");
		search_query = search_query.replaceAll("\\t", "\\\\t");
		search_query = search_query.replaceAll("\\00", "\\0");

		// change all search_terms to lower case
		search_query = search_query.toLowerCase();

		// trim all search terms and remove empty terms
		List<String> search_terms = new ArrayList<String>();

		String[] terms = search_query.trim().split(" ");
		for (String term : terms) {
			if (term != null && !term.trim().isEmpty()) {
				search_terms.add(term.trim());
			}
		}
		return String.join(" ", search_terms);
	}
}
