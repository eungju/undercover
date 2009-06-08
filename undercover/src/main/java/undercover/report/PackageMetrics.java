package undercover.report;

import java.util.Collection;

public class PackageMetrics  {
	private final LazyPackageCount packageCount;
	private BlockMetrics blockMetrics;

	public PackageMetrics(Collection<? extends Item> children, BlockMetrics blockMetrics) {
		packageCount = new LazyPackageCount(children);
		this.blockMetrics = blockMetrics;
	}
	
	public int getCount() {
		return packageCount.value();
	}

	public double getAverageComplexity() {
		return ((double) blockMetrics.getComplexity()) / getCount();
	};
}
