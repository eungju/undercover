package undercover.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import undercover.data.BlockMeta;
import undercover.data.ClassMeta;
import undercover.data.Coverage;
import undercover.data.CoverageData;
import undercover.data.MetaData;
import undercover.data.MetaDataVisitor;
import undercover.data.MethodMeta;

public class ReportDataBuilder implements MetaDataVisitor {
	private final CoverageData coverageData;

	private SourceFinder sourceFinder = new SourceFinder(new ArrayList<File>());
	private String projectName = "Anonymous";
	
	final Map<String, PackageItem> packageItems = new HashMap<String, PackageItem>();
	final Map<String, ClassItem> classItems = new HashMap<String, ClassItem>();
	final Map<String, SourceItem> sourceItems = new HashMap<String, SourceItem>();
	final List<ClassMeta> anonymousClasses = new ArrayList<ClassMeta>();

	ReportData reportData;
	ProjectItem projectItem;
	PackageItem packageItem;
	SourceItem sourceItem;
	ClassItem classItem;
	Coverage classCoverage;
	MethodItem methodItem;
	int methodIndex;
	int blockIndex;
	
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
		Map<String, ClassItem> namedClassItems = new HashMap<String, ClassItem>(classItems);
		for (ClassMeta each : anonymousClasses) {
			ClassItem outerClass = classItems.get(each.outer.className);
			ClassItem nestedClass = classItems.get(each.name);
			if (each.outer.isMethod()) {
				MethodItem outerMethod = outerClass.getMethod(each.outer.methodName);
				outerMethod.addClass(nestedClass);
			} else {
				outerClass.addClass(nestedClass);
			}
			namedClassItems.remove(each.name);
		}
		
		reportData = new ReportData(projectItem, namedClassItems, sourceItems);
	}

	public void visitEnter(ClassMeta classMeta) {
		String packageName = classMeta.getPackageName();
		packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
		}

		SourceFile sourceFile = sourceFinder.findSourceFile(classMeta); 
		sourceItem = sourceItems.get(sourceFile.path);
		if (sourceItem == null) {
			sourceItem = new SourceItem(sourceFile);
		}
		
		if (classMeta.isAnonymous()) {
			anonymousClasses.add(classMeta);
		}

		classItem = new ClassItem(classMeta.name);
		classItem.setSourceFile(sourceFile);
		classCoverage = coverageData.getCoverage(classMeta.name);
		methodIndex = 0;
	}

	public void visitLeave(ClassMeta classMeta) {
		if (!classMeta.isAnonymous()) {
			packageItem.addClass(classItem);
			sourceItem.addClass(classItem);
		}
		
		if (!packageItems.containsKey(packageItem.getName())) {
			packageItems.put(packageItem.getName(), packageItem);
			projectItem.addPackage(packageItem);
		}

		if (!sourceItems.containsKey(sourceItem.getName())) {
			sourceItems.put(sourceItem.getName(), sourceItem);
		}
		
		classItems.put(classItem.getName(), classItem);
	}

	public void visitEnter(MethodMeta methodMeta) {
		blockIndex = 0;
	}

	public void visitLeave(MethodMeta methodMeta) {
		int coveredBlockCount = classCoverage == null ? 0 : classCoverage.countCoveredBlocks(methodIndex);
		methodItem = new MethodItem(methodMeta, coveredBlockCount);
		classItem.addMethod(methodItem);
		methodIndex++;
	}

	public void visit(BlockMeta blockMeta) {
		int executionCount = classCoverage == null ? 0 : classCoverage.countExecution(methodIndex, blockIndex);
		sourceItem.addBlock(blockMeta, executionCount);
		blockIndex++;
	}
}
