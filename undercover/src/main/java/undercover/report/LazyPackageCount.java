package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyPackageCount extends LazyValue<Integer> {
	private Collection<? extends Item> children;

	public LazyPackageCount(Collection<? extends Item> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		int result = 0;
		for (Item each : children) {
			if (each instanceof PackageItem) {
				result++;
			} else {
				result += each.getPackageMetrics().getCount();
			}
		}
		return result;
	}
}