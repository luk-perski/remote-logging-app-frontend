package utils.app.page;

public class BreadcrumbElement {

	private String title;

	private String url;

	private boolean active;

	public BreadcrumbElement(String title, String url, boolean active) {
		this.title = title;
		this.url = url;
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return url;
	}

	public boolean isActive() {
		return active;
	}
}
