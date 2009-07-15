package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import undercover.support.LazyValue;
import undercover.support.Proportion;

public class MethodMetrics {
	private final BlockMetrics blockMetrics;
	private final LazyValue<List<MethodItem>> methods;
	private final LazyValue<Proportion> coverage;

	public MethodMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		this.blockMetrics = blockMetrics;
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
		coverage = new LazyValue<Proportion>() {
			@Override
			protected Proportion calculate() {
				int part = 0;
				int whole = 0;
				for (MethodItem each : methods.value()) {
					if (each.getBlockMetrics().isExecutable()) {
						whole++;
					}
					if (each.getBlockMetrics().isExecuted()) {
						part++;
					}
				}
				return new Proportion(part, whole);
			}
		};
	}

	public int getCount() {
		return methods.value().size();
	}
	
	public int getMaximumComplexity() {
		return getCount() == 0 ? 0 : Collections.max(methods.value(), new ItemComplexityAscending()).getBlockMetrics().getComplexity();
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

	public Proportion getCoverage() {
		return coverage.value();
	}
}
