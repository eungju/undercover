package undercover.report;

import java.util.Collection;

public abstract class SourceMeasure extends ClassMeasure {
	private LazyClassCount classCount;

	public void initializeSourceMeasure(Collection<? extends ClassMeasure> children) {
		super.initializeClassMeasure(children);
		classCount = new LazyClassCount(children);
	}
	
	public int getClassCount() {
		return classCount.value();
	}

	public double getAverageClassComplexity() {
		return ((double) getComplexity()) / getClassCount();
	};
}
