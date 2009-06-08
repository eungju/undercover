package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class ClassMetrics {
	private final LazyClassCount classCount;
	private final BlockMetrics blockMetrics;
	private final LazyValue<Integer> maximumComplexity;

	public ClassMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		classCount = new LazyClassCount(children);
		this.blockMetrics = blockMetrics;
		maximumComplexity = new LazyValue<Integer>() {
			protected Integer calculate() {
				int result = 0;
				for (Item each : children) {
					int complexity;
					if (each instanceof ClassItem) {
						complexity = each.getBlockMetrics().getComplexity();
					} else {
						complexity = each.getClassMetrics().getMaximumComplexity();
					}
					result = Math.max(result, complexity);
				}
				return result;
			}
		};
	}
	
	public int getCount() {
		return classCount.value();
	}
	
	public int getMaximumComplexity() {
		return maximumComplexity.value();
	}

	public double getAverageComplexity() {
		return ((double) blockMetrics.getComplexity()) / getCount();
	};
}
