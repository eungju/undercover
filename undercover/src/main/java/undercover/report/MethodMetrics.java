package undercover.report;

import java.util.Collection;

public class MethodMetrics {
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	private final BlockMetrics blockMetrics;

	public MethodMetrics(Collection<? extends Item> children, BlockMetrics blockMetrics) {
		methodCount = new LazyMethodCount(children);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(children);
		this.blockMetrics = blockMetrics;
	}

	public int getCount() {
		return methodCount.value();
	}
	
	public int getMaximumComplexity() {
		return maximumMethodComplexity.value();
	}
	
	public double getAverageComplexity() {
		return ((double) blockMetrics.getComplexity()) / getCount();
	};
}
