package undercover.report;

import undercover.metric.MethodMeta;

public class MethodItem extends AbstractItem {
	private final int complexity;
	private final int blockCount;
	private final int coveredBlockCount;
	
	public MethodItem(ClassItem parent, MethodMeta methodMeta, int coveredBlockCount) {
		this(parent.getName() + "." + methodMeta.name, methodMeta.complexity, methodMeta.blocks.size(), coveredBlockCount);
	}
	
	public MethodItem(String name, int complexity, int blockCount, int coveredBlockCount) {
		super(name, name.replaceAll("/", "."));
		this.complexity = complexity;
		this.blockCount = blockCount;
		this.coveredBlockCount = coveredBlockCount;
	}
	
	public String getLinkName() {
		throw new UnsupportedOperationException();
	}

	public int getBlockCount() {
		return blockCount;
	}

	public int getComplexity() {
		return complexity;
	}

	public int getMethodCount() {
		throw new UnsupportedOperationException();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount;
	}
}
