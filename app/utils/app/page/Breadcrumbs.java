package utils.app.page;

import java.util.LinkedList;
import java.util.List;

public class Breadcrumbs {

	private List<BreadcrumbElement> elements;

	public Breadcrumbs() {
		this.elements = new LinkedList<BreadcrumbElement>();
	}

	/**
	 * Allows adding an element to the breadcrumbs structure
	 * 
	 * @param element
	 *            The element to be added
	 * @return *this* for nesting
	 */
	public Breadcrumbs add(BreadcrumbElement element) {
		this.elements.add(element);
		return this;
	}
	
	public List<BreadcrumbElement> getElements(){
		return this.elements;
	}
}
