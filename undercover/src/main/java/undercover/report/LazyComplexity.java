package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyComplexity extends LazyValue<Integer> {
	private Collection<? extends MethodMeasure> children;

	public LazyComplexity(Collection<? extends MethodMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (MethodMeasure each : children) {
			result += each.getComplexity();
		}
		return result;
	}
}