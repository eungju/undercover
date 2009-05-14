package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectItem extends CompositeItem {
	public final List<PackageItem> packages;
	
	public ProjectItem() {
		super("project", "project");
		packages = new ArrayList<PackageItem>();
	}
	
	public void addPackage(PackageItem packageItem) {
		packages.add(packageItem);
	}

	protected Collection<Item> getItems() {
		return (Collection) packages;
	}
}
