package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;

public class ClassMetrics {
	private final LazyClassCount classCount;
	private final BlockMetrics blockMetrics;
	private final LazyValue<Integer> maximumComplexity;
	private final LazyValue<List<ClassItem>> classes;
	
	public ClassMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		this.blockMetrics = blockMetrics;
		classCount = new LazyClassCount(children);
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
		classes = new LazyValue<List<ClassItem>>() {
			protected List<ClassItem> calculate() {
				List<ClassItem> result = new ArrayList<ClassItem>();
				for (Item each : children) {
					if (each instanceof ClassItem) {
						result.add((ClassItem) each);
					} else {
						result.addAll(each.getClassMetrics().classes.value());
					}
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
	
	public double getVariance() {
		List<ClassItem> elements = classes.value();
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
