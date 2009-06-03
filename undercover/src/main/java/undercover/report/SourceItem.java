package undercover.report;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import undercover.metric.BlockMeta;

public class SourceItem extends SourceMeasure {
	private final SourceFile sourceFile;
	public final SortedSet<ClassItem> classes;
	private final LineCoverageAnalysis lineCoverageAnalysis = new LineCoverageAnalysis();
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	
	public SourceItem(SourceFile sourceFile) {
		this.sourceFile = sourceFile;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		complexity = new LazyComplexity(classes);
		blockCount = new LazyBlockCount(classes);
		coveredBlockCount = new LazyCoveredBlockCount(classes);
		methodCount = new LazyMethodCount(classes);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(classes);
	}
	
	public String getName() {
		return sourceFile.path;
	}
	
	public String getDisplayName() {
		return getName();
	}

	public String getLinkName() {
		return getName().replaceAll("/", ".");
	}
	
	public String getLanguage() {
		return FilenameUtils.getExtension(sourceFile.path);
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
	
	public int getMaximumMethodComplexity() {
		return maximumMethodComplexity.value();
	}
	
	public List<SourceLine> getLines() {
		List<SourceLine> lines = new ArrayList<SourceLine>();
		if (sourceFile.isExist()) {
			Reader input = null;
			try {
				input = sourceFile.openReader();
				int lineNumber = 1;
				for (String each : (List<String>) IOUtils.readLines(input)) {
					lines.add(new SourceLine(lineNumber, each, lineCoverageAnalysis.getLine(lineNumber)));
					lineNumber++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		return lines;
	}
}
