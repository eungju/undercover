package undercover.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PackageItem extends CompositeItem {
	public final SortedSet<ClassItem> classes;
	
	public PackageItem(String name) {
		super(name, name.replaceAll("/", "."));
		this.classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
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

	public static final Comparator<PackageItem> DISPLAY_ORDER = new Comparator<PackageItem>() {
		public int compare(PackageItem a, PackageItem b) {
			return a.name.compareTo(b.name);
		}
	};
}
