package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;

public class PackageMetrics  {
	private final LazyPackageCount packageCount;
	private final BlockMetrics blockMetrics;
	private final LazyValue<Integer> maximumComplexity;
	private final LazyValue<List<PackageItem>> packages;

	public PackageMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		this.blockMetrics = blockMetrics;
		packageCount = new LazyPackageCount(children);
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
		packages = new LazyValue<List<PackageItem>>() {
			protected List<PackageItem> calculate() {
				List<PackageItem> result = new ArrayList<PackageItem>();
				for (Item each : children) {
					if (each instanceof PackageItem) {
						result.add((PackageItem) each);
					} else {
						result.addAll(each.getPackageMetrics().packages.value());
					}
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
	
	public double getVariance() {
		List<PackageItem> elements = packages.value();
		double avg = getAverageComplexity();
		double v = 0;
		for (Item each : elements) {
			int x = each.getBlockMetrics().getComplexity();
			v += Math.pow((x - avg), 2);
		}
		return v / elements.size();
	}
	
	public double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}
}
