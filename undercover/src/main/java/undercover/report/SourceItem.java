package undercover.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import undercover.data.BlockMeta;
import undercover.support.IOUtils;

public class SourceItem implements Item {
	private final SourceFile sourceFile;
	public final SortedSet<ClassItem> classes;
	private BlockMetrics blockMetrics;
	private MethodMetrics methodMetrics;
	private ClassMetrics classMetrics;
	private List<SourceLine> lines;
	
	public SourceItem(SourceFile sourceFile) {
		this.sourceFile = sourceFile;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		blockMetrics = new BlockMetrics(classes);
		methodMetrics = new MethodMetrics(classes);
		classMetrics = new ClassMetrics(classes);
		lines = readSourceLines(sourceFile);
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
	
	public String getSimpleName() {
		int pos = sourceFile.path.lastIndexOf('/');
		return pos < 0 ? sourceFile.path : sourceFile.path.substring(pos + 1); 
	}

	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}
	
	public void addBlock(BlockMeta blockMeta, int executionCount) {
		if (sourceFile.isExist()) {
			addUpLineCoverage(blockMeta, executionCount);
		}
	}
	
	public List<SourceLine> getLines() {
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
	
	List<SourceLine> readSourceLines(SourceFile sourceFile) {
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
					lines.add(new SourceLine(lineNumber, each));
					lineNumber++;
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to read source file " + sourceFile.path, e);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		return lines;
	}
	
	void addUpLineCoverage(BlockMeta blockMeta, int executionCount) {
		for (int lineNumber : blockMeta.lines) {
			if (lines.size() < lineNumber) {
				continue;
			}
			SourceLine sourceLine = lines.get(lineNumber - 1);
			sourceLine.addBlock(executionCount);
		}
	}
}
