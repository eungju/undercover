package undercover.report;

import java.util.Collection;
import java.util.Map;

public class ReportData {
	private final ProjectItem projectItem;
	private final Map<String, PackageItem> packageItems;
	private final Map<String, ClassItem> classItems;
	private final Map<String, SourceItem> sourceItems;
	
	public ReportData(ProjectItem projectItem, Map<String, PackageItem> packageItems, Map<String, ClassItem> classItems, Map<String, SourceItem> sourceItems) {
		this.projectItem = projectItem;
		this.packageItems = packageItems;
		this.classItems = classItems;
		this.sourceItems = sourceItems;
	}
	
	public ProjectItem getProject() {
		return projectItem;
	}

	public Collection<PackageItem> getAllPackages() {
		return packageItems.values();
	}
	
	public PackageItem getPackage(String name) {
		return packageItems.get(name);
	}
	
	public Collection<ClassItem> getAllClasses() {
		return classItems.values();
	}
	
	public ClassItem getClass(String name) {
		return classItems.get(name);
	}

	public Collection<SourceItem> getAllSources() {
		return sourceItems.values();
	}
}
