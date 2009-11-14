package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.PackageItem;

public class PackageClassListPage extends ClassListPage {
	private final PackageItem packageItem;

	public PackageClassListPage(PackageItem packageItem) {
		this.packageItem = packageItem;
	}
	
	@Override
	public String getTitle() {
		return "Classes of " + packageItem.getDisplayName();
	}
	
	@Override
	public Collection<ClassItem> getClassItems() {
		return packageItem.classes;
	}
}
