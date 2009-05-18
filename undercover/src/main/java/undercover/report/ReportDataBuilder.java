package undercover.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import undercover.metric.BlockMeta;
import undercover.metric.ClassMeta;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;
import undercover.metric.MetaDataVisitor;
import undercover.metric.MethodMeta;

public class ReportDataBuilder implements MetaDataVisitor {
	private final CoverageData coverageData;

	private SourceFinder sourceFinder = new SourceFinder(new ArrayList<File>());
	private String projectName = "Anonymous";
	
	final Map<String, PackageItem> packageItems = new HashMap<String, PackageItem>();
	final Map<String, ClassItem> classItems = new HashMap<String, ClassItem>();

	private ReportData reportData;
	private ProjectItem projectItem;
	private PackageItem packageItem;
	private ClassItem classItem;
	private MethodItem methodItem;
	
	public ReportDataBuilder(CoverageData coverageData) {
		this.coverageData = coverageData;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public void setSourceFinder(SourceFinder sourceFinder) {
		this.sourceFinder = sourceFinder;
	}
	
	public ReportData getReportData() {
		return reportData;
	}

	public void visitEnter(MetaData metaData) {
		projectItem = new ProjectItem(projectName);
	}

	public void visitLeave(MetaData metaData) {
		reportData = new ReportData(projectItem, packageItems, classItems);
	}

	public void visitEnter(ClassMeta classMeta) {
		String packageName = classMeta.name().substring(0, classMeta.name().lastIndexOf("/"));
		packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
		}
		
		classItem = new ClassItem(packageItem, classMeta.name(), sourceFinder.findSourcePath(classMeta));
	}

	public void visitLeave(ClassMeta classLeave) {
		if (!packageItems.containsKey(packageItem.getName())) {
			projectItem.addPackage(packageItem);
			packageItems.put(packageItem.getName(), packageItem);
		}
		
		packageItem.addClass(classItem);
		classItems.put(classItem.getName(), classItem);
	}

	public void visitEnter(MethodMeta methodMeta) {
		methodItem = new MethodItem(classItem, methodMeta, coverageData);
	}

	public void visitLeave(MethodMeta methodLeave) {
		classItem.addMethod(methodItem);
	}

	public void visit(BlockMeta blockMeta) {
	}
}
