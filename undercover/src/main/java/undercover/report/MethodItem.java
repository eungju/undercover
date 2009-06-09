package undercover.report;

import java.util.ArrayList;
import java.util.List;

import undercover.data.MethodMeta;

public class MethodItem implements Item {
	private final String name;
	public final List<ClassItem> classes;
	private final BlockMetrics blockMetrics;
	
	public MethodItem(MethodMeta methodMeta, int coveredBlockCount) {
		this(methodMeta.name + methodMeta.descriptor, methodMeta.complexity, methodMeta.blocks.size(), coveredBlockCount);
	}
	
	public MethodItem(String name, int complexity, int blockCount, int coveredBlockCount) {
		this.name = name;
		classes = new ArrayList<ClassItem>();
		blockMetrics = new BlockMetrics(complexity, blockCount, coveredBlockCount, classes);
	}

	public void addClass(ClassItem classItem) {
		classes.add(classItem);
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return name.replaceAll("/", ".");
	}
	
	public String getLinkName() {
		throw new UnsupportedOperationException();
	}

	public BlockMetrics getBlockMetrics() {
		return blockMetrics;
	}
	
	public MethodMetrics getMethodMetrics() {
		return null;
	}
	
	public ClassMetrics getClassMetrics() {
		return null;
	}
	
	public PackageMetrics getPackageMetrics() {
		return null;
	}
}
