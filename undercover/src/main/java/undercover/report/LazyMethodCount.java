package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyMethodCount extends LazyValue<Integer> {
	private Collection<? extends MethodMeasure> children;

	public LazyMethodCount(Collection<? extends MethodMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (MethodMeasure each : children) {
			if (each instanceof ClassMeasure) {
				result += ((ClassMeasure) each).getMethodCount();
			} else {
				result += 1;
			}
		}
		return result;
	}
}