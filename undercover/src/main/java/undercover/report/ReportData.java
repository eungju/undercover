package undercover.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import undercover.metric.ClassMetric;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;
import undercover.metric.MethodMetric;

public class ReportData {
	private final MetaData metaData;
	private final CoverageData coverageData;

	private Map<String, PackageItem> packageItems = new HashMap<String, PackageItem>();
	private Map<String, ClassItem> classItems = new HashMap<String, ClassItem>();
	
	public ReportData(MetaData metaData, CoverageData coverageData) {
		this.metaData = metaData;
		this.coverageData = coverageData;

		for (ClassMetric each : metaData.getAllClasses()) {
			addClass(each);
		}
	}
	
	public void addClass(ClassMetric classMetric) {
		String packageName = classMetric.name().substring(0, classMetric.name().lastIndexOf("/"));
		PackageItem packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
			packageItems.put(packageItem.name, packageItem);
		}
		
		ClassItem classItem = new ClassItem(packageItem, classMetric);
		packageItem.addClass(classItem);
		classItems.put(classItem.name, classItem);
		
		for (MethodMetric each : classMetric.methods()) {
			MethodItem methodItem = new MethodItem(classItem, each, coverageData);
			classItem.addMethod(methodItem);
		}
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
