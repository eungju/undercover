package undercover.report;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PackageItem extends PackageMeasure {
	private final String name;
	public final SortedSet<ClassItem> classes;
	
	public PackageItem(String name) {
		this.name = name;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		initializePackageMeasure(classes);
	}
	
	public void addClass(ClassItem child) {
		classes.add(child);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return name.replaceAll("/", ".");
	}
	
	public String getLinkName() {
		return name.replaceAll("/", ".");
	}

	public static final Comparator<PackageItem> DISPLAY_ORDER = new Comparator<PackageItem>() {
		public int compare(PackageItem a, PackageItem b) {
			return a.name.compareTo(b.name);
		}
	};
}
