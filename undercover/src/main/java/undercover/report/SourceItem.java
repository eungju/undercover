package undercover.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;

import undercover.data.BlockMeta;

public class SourceItem implements Item {
	private final SourceFile sourceFile;
	public final SortedSet<ClassItem> classes;
	private final LineCoverageAnalysis lineCoverageAnalysis = new LineCoverageAnalysis();
	private BlockMetrics blockMetrics;
	private MethodMetrics methodMetrics;
	private ClassMetrics classMetrics;
	
	public SourceItem(SourceFile sourceFile) {
		this.sourceFile = sourceFile;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		blockMetrics = new BlockMetrics(classes);
		methodMetrics = new MethodMetrics(classes, blockMetrics);
		classMetrics = new ClassMetrics(classes, blockMetrics);
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

	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}
	
	public void addBlock(BlockMeta blockMeta, int blockCoverage) {
		lineCoverageAnalysis.analyze(blockMeta, blockCoverage);
	}
	
	public List<SourceLine> getLines() {
		List<SourceLine> lines = new ArrayList<SourceLine>();
		if (sourceFile.isExist()) {
			BufferedReader input = null;
			try {
				input = new BufferedReader(sourceFile.openReader());
				int lineNumber = 1;
				while (true) {
					String each = input.readLine();
					if (each == null) {
						break;
					}
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

	public BlockMetrics getBlockMetrics() {
		return blockMetrics;
	}

	public MethodMetrics getMethodMetrics() {
		return methodMetrics;
	}

	public ClassMetrics getClassMetrics() {
		return classMetrics;
	}
	
	public PackageMetrics getPackageMetrics() {
		return null;
	}
}
