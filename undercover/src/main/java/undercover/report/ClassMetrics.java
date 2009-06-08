package undercover.report;

import java.util.Collection;

public class ClassMetrics {
	private final LazyClassCount classCount;
	private final BlockMetrics blockMetrics;

	public ClassMetrics(Collection<? extends Item> children, BlockMetrics blockMetrics) {
		classCount = new LazyClassCount(children);
		this.blockMetrics = blockMetrics;
	}
	
	public int getCount() {
		return classCount.value();
	}

	public double getAverageComplexity() {
		return ((double) blockMetrics.getComplexity()) / getCount();
	};
}
