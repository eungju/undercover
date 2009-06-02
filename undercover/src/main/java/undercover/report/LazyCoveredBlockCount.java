package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyCoveredBlockCount extends LazyValue<Integer> {
	private Collection<? extends MethodMeasure> children;

	public LazyCoveredBlockCount(Collection<? extends MethodMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (MethodMeasure each : children) {
			result += each.getCoveredBlockCount();
		}
		return result;
	}
}