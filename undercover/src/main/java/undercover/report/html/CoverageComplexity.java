package undercover.report.html;

import java.util.ArrayList;
import java.util.Collection;

import undercover.report.ClassItem;

public class CoverageComplexity {
	private Collection<ClassItem> items = new ArrayList<ClassItem>();

	public CoverageComplexity(Collection<ClassItem> items) {
		for (ClassItem each : items) {
			add(each);
		}
	}

	public void add(ClassItem item) {
		items.add(item);
	}
	
	public Collection<ClassItem> getItems() {
		return items;
	}
}
