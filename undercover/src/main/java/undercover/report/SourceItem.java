package undercover.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import undercover.metric.BlockMeta;

public class SourceItem extends SourceMeasure {
	private final String name;
	public final File file;
	public final SortedSet<ClassItem> classes;
	private final LineCoverageAnalysis lineCoverageAnalysis = new LineCoverageAnalysis();
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;
	private final LazyMethodCount methodCount;
	
	public SourceItem(SourceFile sourceFile) {
		this(sourceFile.path, sourceFile.file);
	}
	
	public SourceItem(String name, File file) {
		this.name = name;
		this.file = file;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		complexity = new LazyComplexity(classes);
		blockCount = new LazyBlockCount(classes);
		coveredBlockCount = new LazyCoveredBlockCount(classes);
		methodCount = new LazyMethodCount(classes);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return name;
	}

	public String getLinkName() {
		return name.replaceAll("/", ".");
	}
	
	public String getLanguage() {
		return FilenameUtils.getExtension(name);
	}

	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}
	
	public void addBlock(BlockMeta blockMeta, int blockCoverage) {
		lineCoverageAnalysis.analyze(blockMeta, blockCoverage);
	}

	public int getComplexity() {
		return complexity.value();
	}

	public int getBlockCount() {
		return blockCount.value();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount.value();
	}

	public int getMethodCount() {
		return methodCount.value();
	}
	
	public List<SourceLine> getLines() {
		List<SourceLine> lines = new ArrayList<SourceLine>();
		if (file != null) {
			try {
				int lineNumber = 1;
				for (String each : (List<String>) FileUtils.readLines(file, "UTF-8")) {
					lines.add(new SourceLine(lineNumber, each, lineCoverageAnalysis.getLine(lineNumber)));
					lineNumber++;
				}
			} catch (IOException e) {
				//TODO: warning
			}
		}
		return lines;
	}
}
