package undercover.report;

import undercover.metric.MethodMeta;

public class MethodItem implements Item {
	private final String name;
	private final BlockMetrics blockMetrics;
	
	public MethodItem(ClassItem parent, MethodMeta methodMeta, int coveredBlockCount) {
		this(parent.getName() + "." + methodMeta.name, methodMeta.complexity, methodMeta.blocks.size(), coveredBlockCount);
	}
	
	public MethodItem(String name, int complexity, int blockCount, int coveredBlockCount) {
		this.name = name;
		blockMetrics = new BlockMetrics.Leaf(complexity, blockCount, coveredBlockCount);
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		return name.substring(name.lastIndexOf('.') + 1, name.length());
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
