package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyBlockCount extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyBlockCount(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			result += each.getBlockMetrics().getBlockCount();
		}
		return result;
	}
}