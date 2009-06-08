package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyClassCount extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyClassCount(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			if (each instanceof ClassItem) {
				result ++;
			} else {
				result += each.getClassMetrics().getCount();
			}
		}
		return result;
	}
}