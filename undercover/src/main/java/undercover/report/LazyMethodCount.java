package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyMethodCount extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyMethodCount(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			if (each instanceof MethodItem) {
				result++;
			} else {
				result += each.getMethodMetrics().getCount();
			}
		}
		return result;
	}
}