package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyCoveredBlockCount extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyCoveredBlockCount(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			result += each.getBlockMetrics().getCoveredBlockCount();
		}
		return result;
	}
}