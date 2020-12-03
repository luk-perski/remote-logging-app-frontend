package models.db.search;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Model;

@Entity
@Table(name = "search_search_content_class_custom_handler")
public class SearchContentClassCustomHandler extends Model {

	@Id
	@Column(length = SearchContentPrepared.MAX_SIZE_CONTENT_CLASS)
	private String content_class;

	@Column(length = SearchContentPrepared.MAX_SIZE_CONTENT_CLASS)
	private String custom_handler_class;
}
