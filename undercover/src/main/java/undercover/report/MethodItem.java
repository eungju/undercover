package undercover.report;

import undercover.metric.MethodMeta;

public class MethodItem extends MethodMeasure {
	private final String name;
	private final int complexity;
	private final int blockCount;
	private final int coveredBlockCount;
	
	public MethodItem(ClassItem parent, MethodMeta methodMeta, int coveredBlockCount) {
		this(parent.getName() + "." + methodMeta.name, methodMeta.complexity, methodMeta.blocks.size(), coveredBlockCount);
	}
	
	public MethodItem(String name, int complexity, int blockCount, int coveredBlockCount) {
		this.name = name;
		this.complexity = complexity;
		this.blockCount = blockCount;
		this.coveredBlockCount = coveredBlockCount;
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

	public int getBlockCount() {
		return blockCount;
	}

	public int getComplexity() {
		return complexity;
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount;
	}
}
