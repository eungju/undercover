package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyClassCount extends LazyValue<Integer> {
	private Collection<? extends ClassMeasure> children;

	public LazyClassCount(Collection<? extends ClassMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (ClassMeasure each : children) {
			if (each instanceof PackageMeasure) {
				result += ((PackageMeasure) each).getClassCount();
			} else {
				result += 1;
			}
		}
		return result;
	}
}