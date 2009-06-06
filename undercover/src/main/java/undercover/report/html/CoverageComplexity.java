package undercover.report.html;

import java.util.ArrayList;
import java.util.Collection;

import undercover.report.ClassItem;

public class CoverageComplexity {
	private final Collection<ClassItem> items;

	public CoverageComplexity(Collection<ClassItem> items) {
		this();
		addAll(items);
	}

	public CoverageComplexity() {
		items = new ArrayList<ClassItem>();
	}
	
	public void add(ClassItem item) {
		items.add(item);
	}
	
	public void addAll(Collection<ClassItem> items) {
		this.items.addAll(items);
	}
	
	public Collection<ClassItem> getItems() {
		return items;
	}
}
