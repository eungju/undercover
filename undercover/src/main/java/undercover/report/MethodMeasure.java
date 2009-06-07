package undercover.report;

public abstract class MethodMeasure {
	private int complexity;
	private int blockCount;
	private int coveredBlockCount;

	public void initializeMethodMeasure(int complexity, int blockCount, int coveredBlockCount) {
		this.complexity = complexity;
		this.blockCount = blockCount;
		this.coveredBlockCount = coveredBlockCount;
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
	
	public boolean isExecutable() {
		return getBlockCount() > 0;
	}
	
	public double getCoverageRate() {
		return getBlockCount() == 0 ? 1 : ((double) getCoveredBlockCount()) / getBlockCount();
	}
	
	public double getRisk() {
		return getComplexity() + (getComplexity() * (1 - getCoverageRate()));
	}
}
