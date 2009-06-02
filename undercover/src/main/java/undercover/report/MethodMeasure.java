package undercover.report;

public abstract class MethodMeasure {
	public abstract int getComplexity();
	
	public abstract int getBlockCount();
	
	public abstract int getCoveredBlockCount();
	
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
