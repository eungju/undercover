package undercover.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import undercover.metric.BlockMeta;

public class SourceItem extends CompositeItem {
	public final String language;
	public final File file;
	public final SortedSet<ClassItem> classes;
	private final LineCoverageAnalysis lineCoverageAnalysis = new LineCoverageAnalysis();
	
	public SourceItem(SourceFile sourceFile) {
		this(sourceFile.path, sourceFile.file);
	}
	
	public SourceItem(String name, File file) {
		super(name, name);
		this.file = file;
		language = FilenameUtils.getExtension(name);
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
	}

	public String getLinkName() {
		return name.replaceAll("/", ".");
	}

	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}
	
	public void addBlock(BlockMeta blockMeta, int blockCoverage) {
		lineCoverageAnalysis.analyze(blockMeta, blockCoverage);
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

	protected Collection<Item> getItems() {
		return (Collection) classes;
	}
}
