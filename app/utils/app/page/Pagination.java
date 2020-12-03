package utils.app.page;

import io.ebean.PagedList;

public class Pagination {

	private int number_of_pages;

	private int current_page;

	private boolean has_next_page;

	private boolean has_previous_page;

	private int number_of_records;

	private int current_records_start;

	private int current_records_end;

	public Pagination(PagedList<?> paged_list) {
		this.number_of_pages = paged_list.getTotalPageCount();
		this.current_page = paged_list.getPageIndex() + 1;
		this.has_next_page = paged_list.hasNext();
		this.has_previous_page = paged_list.hasPrev();
		this.number_of_records = paged_list.getTotalCount();
		this.current_records_start = paged_list.getPageIndex() * paged_list.getPageSize();
		if (this.number_of_records > this.current_records_start + paged_list.getPageSize()) {
			this.current_records_end = this.current_records_start + paged_list.getPageSize();
		} else {
			this.current_records_end = this.number_of_records;
		}
	}

	public int getNumberOfPages() {
		return number_of_pages;
	}

	public int getCurrentPage() {
		return current_page;
	}

	public boolean hasNextPage() {
		return has_next_page;
	}

	public boolean hasPreviousPage() {
		return has_previous_page;
	}

	public int getNumberOfRecords() {
		return number_of_records;
	}

	public int getCurrentRecordsStart() {
		return current_records_start;
	}

	public int getCurrentRecordsEnd() {
		return current_records_end;
	}
}
