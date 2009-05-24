package undercover.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import undercover.metric.BlockMeta;
import undercover.metric.ClassMeta;
import undercover.metric.Coverage;
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
	int methodIndex;
	int blockIndex;
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

		SourceFile sourceFile = sourceFinder.findSourceFile(classMeta); 
		sourceItem = sourceItems.get(sourceFile.path);
		if (sourceItem == null) {
			sourceItem = new SourceItem(sourceFile);
		}
		
		classItem = new ClassItem(classMeta.name(), sourceFile);
		methodIndex = 0;
	}

	public void visitLeave(ClassMeta classMeta) {
		packageItem.addClass(classItem);
		if (!packageItems.containsKey(packageItem.getName())) {
			packageItems.put(packageItem.getName(), packageItem);
			projectItem.addPackage(packageItem);
		}

		sourceItem.addClass(classItem);
		if (!sourceItems.containsKey(sourceItem.getName())) {
			sourceItems.put(sourceItem.getName(), sourceItem);
		}
		
		classItems.put(classItem.getName(), classItem);
	}

	public void visitEnter(MethodMeta methodMeta) {
		blockIndex = 0;
		coveredBlockCount = 0;
	}

	public void visitLeave(MethodMeta methodMeta) {
		methodItem = new MethodItem(classItem, methodMeta, coveredBlockCount);
		classItem.addMethod(methodItem);
		methodIndex++;
	}

	public void visit(BlockMeta blockMeta) {
		Coverage coverage = coverageData.getCoverage(classItem.name);
		if (coverage.blocks[methodIndex][blockIndex] > 0) {
			coveredBlockCount++;
		}
		sourceItem.addBlock(blockMeta, coverage.blocks[methodIndex][blockIndex]);
		blockIndex++;
	}
}
