package undercover.report;

import java.util.Collection;

public abstract class ClassMeasure extends MethodMeasure {
	private LazyComplexity complexity;
	private LazyBlockCount blockCount;
	private LazyCoveredBlockCount coveredBlockCount;
	private LazyMethodCount methodCount;
	private LazyMaximumMethodComplexity maximumMethodComplexity;

	public void initializeClassMeasure(Collection<? extends MethodMeasure> children) {
		complexity = new LazyComplexity(children);
		blockCount = new LazyBlockCount(children);
		coveredBlockCount = new LazyCoveredBlockCount(children);
		methodCount = new LazyMethodCount(children);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(children);
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
	
	public double getAverageMethodComplexity() {
		return ((double) getComplexity()) / getMethodCount();
	};
}
