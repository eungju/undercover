package undercover.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import undercover.metric.ClassMeta;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;
import undercover.metric.MethodMeta;

public class ReportData {
	private final MetaData metaData;
	private final CoverageData coverageData;
	private SourceFinder sourceFinder;

	private final ProjectItem projectItem;
	private final Map<String, PackageItem> packageItems = new HashMap<String, PackageItem>();
	private final Map<String, ClassItem> classItems = new HashMap<String, ClassItem>();
	
	public ReportData(MetaData metaData, CoverageData coverageData, String projectName, SourceFinder sourceFinder) {
		this.metaData = metaData;
		this.coverageData = coverageData;
		this.sourceFinder = sourceFinder;

		projectItem = new ProjectItem(projectName);
		for (ClassMeta each : metaData.getAllClasses()) {
			addClass(each);
		}
	}
	
	public void addClass(ClassMeta classMeta) {
		String packageName = classMeta.name().substring(0, classMeta.name().lastIndexOf("/"));
		PackageItem packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
			projectItem.addPackage(packageItem);
			packageItems.put(packageItem.name, packageItem);
		}
		
		ClassItem classItem = new ClassItem(packageItem, classMeta.name(), sourceFinder.findSourcePath(classMeta));
		packageItem.addClass(classItem);
		classItems.put(classItem.name, classItem);
		
		for (MethodMeta each : classMeta.methods()) {
			MethodItem methodItem = new MethodItem(classItem, each, coverageData);
			classItem.addMethod(methodItem);
		}
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
}
