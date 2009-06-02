package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyMaximumMethodComplexity extends LazyValue<Integer> {
	private Collection<? extends MethodMeasure> children;

	public LazyMaximumMethodComplexity(Collection<? extends MethodMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (MethodMeasure each : children) {
			int complexity;
			if (each instanceof ClassMeasure) {
				complexity = ((ClassMeasure) each).getMaximumMethodComplexity();
			} else {
				complexity = each.getComplexity();
			}
			result = Math.max(result, complexity);
		}
		return result;
	}
}