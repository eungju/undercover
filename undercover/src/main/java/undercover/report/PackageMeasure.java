package undercover.report;

import java.util.Collection;

public abstract class PackageMeasure extends ClassMeasure {
	private LazyClassCount classCount;

	public void initializePackageMeasure(Collection<? extends ClassMeasure> children) {
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
