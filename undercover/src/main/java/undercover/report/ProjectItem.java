package undercover.report;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProjectItem extends CompositeItem {
	public final SortedSet<PackageItem> packages;
	
	public ProjectItem(String name) {
		super(name, name);
		packages = new TreeSet<PackageItem>(PackageItem.DISPLAY_ORDER);
	}
	
	public String getLinkName() {
		return "";
	}
	
	public void addPackage(PackageItem packageItem) {
		packages.add(packageItem);
	}

	protected Collection<Item> getItems() {
		return (Collection) packages;
	}
}
