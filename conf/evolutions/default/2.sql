# --- !Ups
create fulltext index livesearch_index_1 on search_search_content_prepared (title);
create fulltext index livesearch_index_2 on search_search_content_prepared (s1, s2, slug);
create fulltext index livesearch_index on search_search_content_prepared (title, s1, s2, slug);