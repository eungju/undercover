package undercover.report;



public abstract class ClassMeasure extends MethodMeasure {
	public abstract int getMethodCount();
	public abstract int getMaximumMethodComplexity();
	
	public double getAverageMethodComplexity() {
		return ((double) getComplexity()) / getMethodCount();
	};
}
