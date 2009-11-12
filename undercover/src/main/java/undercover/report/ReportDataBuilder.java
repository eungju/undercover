package undercover.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import undercover.data.BlockMeta;
import undercover.data.ClassMeta;
import undercover.data.MetaData;
import undercover.data.MetaDataVisitor;
import undercover.data.MethodMeta;
import undercover.runtime.Coverage;
import undercover.runtime.CoverageData;
import undercover.support.JdkLogger;
import undercover.support.Logger;

public class ReportDataBuilder implements MetaDataVisitor {
	private Logger logger = new JdkLogger();
	private final MetaData metaData;	
	private final CoverageData coverageData;
	private SourceFinder sourceFinder = new SourceFinder(new ArrayList<File>());
	private String projectName = "Anonymous";
	
	final Map<String, PackageItem> packageItems = new HashMap<String, PackageItem>();
	final Map<String, ClassItem> classItems = new HashMap<String, ClassItem>();
	final Map<String, SourceItem> sourceItems = new HashMap<String, SourceItem>();
	final List<ClassMeta> anonymousClasses = new ArrayList<ClassMeta>();

	ReportData reportData;
	PackageItem packageItem;
	SourceItem sourceItem;
	ClassItem classItem;
	Coverage classCoverage;
	MethodItem methodItem;
	int methodIndex;
	int blockIndex;
	
	public ReportDataBuilder(File metaDataFile, File coverageDataFile) throws IOException {
		metaData = MetaData.load(metaDataFile);
		coverageData = coverageDataFile.exists() ? CoverageData.load(coverageDataFile) : new CoverageData();
	}
	
	public ReportDataBuilder(MetaData metaData, CoverageData coverageData) {
		this.metaData = metaData;
		this.coverageData = coverageData;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public void setSourceFinder(SourceFinder sourceFinder) {
		this.sourceFinder = sourceFinder;
	}
	
	public ReportData build() {
		metaData.accept(this);
		return reportData;
	}
	
	public void visitEnter(MetaData metaData) {
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
		
		reportData = new ReportData(projectName, packageItems.values(), namedClassItems.values(), sourceItems.values());
	}

	public void visitEnter(ClassMeta classMeta) {
		logger.debug("Reading metadata for class " + classMeta.name);
		
		String packageName = classMeta.getPackageName();
		packageItem = packageItems.get(packageName);
		if (packageItem == null) {
			packageItem = new PackageItem(packageName);
		}

		SourceFile sourceFile = sourceFinder.findSourceFile(classMeta); 
		sourceItem = sourceItems.get(sourceFile.path);
		if (sourceItem == null) {
			sourceItem = new SourceItem(sourceFile);
			logger.debug(sourceItem.getName() + " has " + sourceItem.getLines().size() + " lines.");
		}
		
		if (classMeta.isAnonymous()) {
			anonymousClasses.add(classMeta);
		}

		classItem = new ClassItem(classMeta.name);
		classItem.setSource(sourceItem);
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
		}

		if (!sourceItems.containsKey(sourceItem.getName())) {
			sourceItems.put(sourceItem.getName(), sourceItem);
		}
		
		classItems.put(classItem.getName(), classItem);
	}

	public void visitEnter(MethodMeta methodMeta) {
		logger.debug("Reading metadata for method " + methodMeta.name + methodMeta.descriptor);
		blockIndex = 0;
	}

	public void visitLeave(MethodMeta methodMeta) {
		int coveredBlockCount = classCoverage == null ? 0 : classCoverage.countCoveredBlocks(methodIndex);
		methodItem = new MethodItem(methodMeta, coveredBlockCount);
		classItem.addMethod(methodItem);
		methodIndex++;
	}

	public void visit(BlockMeta blockMeta) {
		logger.debug("Reading metadata for block " + blockMeta.toString());
		int executionCount = classCoverage == null ? 0 : classCoverage.countExecution(methodIndex, blockIndex);
		sourceItem.addBlock(blockMeta, executionCount);
		blockIndex++;
	}
}
