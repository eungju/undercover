package undercover.report;

import undercover.metric.MethodMeta;

public class MethodItem extends MethodMeasure {
	private final String name;
	
	public MethodItem(ClassItem parent, MethodMeta methodMeta, int coveredBlockCount) {
		this(parent.getName() + "." + methodMeta.name, methodMeta.complexity, methodMeta.blocks.size(), coveredBlockCount);
	}
	
	public MethodItem(String name, int complexity, int blockCount, int coveredBlockCount) {
		this.name = name;
		initializeMethodMeasure(complexity, blockCount, coveredBlockCount);	}

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
}
