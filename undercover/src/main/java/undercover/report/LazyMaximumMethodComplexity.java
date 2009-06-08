package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyMaximumMethodComplexity extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyMaximumMethodComplexity(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			int complexity;
			if (each instanceof MethodItem) {
				complexity = each.getBlockMetrics().getComplexity();
			} else {
				complexity = each.getMethodMetrics().getMaximumComplexity();
			}
			result = Math.max(result, complexity);
		}
		return result;
	}
}