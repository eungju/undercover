package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PackageItem extends CompositeItem {
	public final List<ClassItem> classes;
	
	public PackageItem(String name) {
		super(name, name.replaceAll("\\/", "."));
		this.classes = new ArrayList<ClassItem>();
	}
	
	public void addClass(ClassItem child) {
		classes.add(child);
	}
	
	protected Collection<Item> getItems() {
		return (Collection) classes;
	}

	public String getLink() {
		return "package-" + name.replaceAll("/", ".") + ".html";
	}
}
