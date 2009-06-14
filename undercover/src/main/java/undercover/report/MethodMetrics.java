package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;

public class MethodMetrics {
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	private final BlockMetrics blockMetrics;
	private final LazyValue<List<MethodItem>> methods;

	public MethodMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		this.blockMetrics = blockMetrics;
		methodCount = new LazyMethodCount(children);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(children);
		methods = new LazyValue<List<MethodItem>>() {
			protected List<MethodItem> calculate() {
				List<MethodItem> result = new ArrayList<MethodItem>();
				for (Item each : children) {
					if (each instanceof MethodItem) {
						result.add((MethodItem) each);
					} else {
						result.addAll(each.getMethodMetrics().methods.value());
					}
				}
				return result;
			}
		};
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

	public double getVariance() {
		List<MethodItem> elements = methods.value();
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
