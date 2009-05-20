package undercover.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import undercover.metric.BlockCoverage;
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
	final Map<String, SourceItem> sourceItems = new HashMap<String, SourceItem>();
	
	ReportData reportData;
	ProjectItem projectItem;
	PackageItem packageItem;
	SourceItem sourceItem;
	ClassItem classItem;
	MethodItem methodItem;
	int coveredBlockCount;
	
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
		reportData = new ReportData(projectItem, packageItems, classItems, sourceItems);
	}

	public void visitEnter(ClassMeta classMeta) {
		String packageName = classMeta.name().substring(0, classMeta.name().lastIndexOf("/"));
		packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
		}

		String sourcePath = sourceFinder.findSourcePath(classMeta);
		if (sourcePath == null) {
			sourcePath = sourceFinder.getExpectedSourcePath(classMeta);
		}
		sourceItem = sourceItems.get(sourcePath);
		if (sourceItem == null) {
			sourceItem = new SourceItem(sourcePath, sourceFinder.findSourceFile(sourcePath));
		}
		
		classItem = new ClassItem(classMeta.name(), sourcePath);
	}

	public void visitLeave(ClassMeta classLeave) {
		if (!packageItems.containsKey(packageItem.getName())) {
			projectItem.addPackage(packageItem);
			packageItems.put(packageItem.getName(), packageItem);
		}
		if (!sourceItems.containsKey(sourceItem.getName())) {
			sourceItems.put(sourceItem.getName(), sourceItem);
		}
		
		packageItem.addClass(classItem);
		sourceItem.addClass(classItem);
		classItems.put(classItem.getName(), classItem);
	}

	public void visitEnter(MethodMeta methodMeta) {
		coveredBlockCount = 0;
	}

	public void visitLeave(MethodMeta methodMeta) {
		methodItem = new MethodItem(classItem, methodMeta, coveredBlockCount);
		classItem.addMethod(methodItem);
	}

	public void visit(BlockMeta blockMeta) {
		BlockCoverage blockCoverage = coverageData.getBlock(blockMeta.id());
		if (blockCoverage == null) {
			blockCoverage = new BlockCoverage();
		}
		if (blockCoverage.isTouched()) {
			coveredBlockCount++;
		}
		sourceItem.addBlock(blockMeta, blockCoverage);
	}
}
