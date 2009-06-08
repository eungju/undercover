package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class PackageMetrics  {
	private final LazyPackageCount packageCount;
	private final BlockMetrics blockMetrics;
	private final LazyValue<Integer> maximumComplexity;

	public PackageMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		packageCount = new LazyPackageCount(children);
		this.blockMetrics = blockMetrics;
		maximumComplexity = new LazyValue<Integer>() {
			protected Integer calculate() {
				int result = 0;
				for (Item each : children) {
					int complexity;
					if (each instanceof PackageItem) {
						complexity = each.getBlockMetrics().getComplexity();
					} else {
						complexity = each.getPackageMetrics().getMaximumComplexity();
					}
					result = Math.max(result, complexity);
				}
				return result;
			}
		};
	}

	public int getCount() {
		return packageCount.value();
	}
	
	public int getMaximumComplexity() {
		return maximumComplexity.value();
	}

	public double getAverageComplexity() {
		return ((double) blockMetrics.getComplexity()) / getCount();
	};
}
