package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import undercover.support.LazyValue;
import undercover.support.Proportion;

public class ClassMetrics {
	private final BlockMetrics blockMetrics;
	private final LazyValue<List<ClassItem>> classes;
	private final LazyValue<Integer> maximumComplexity;
	private final LazyValue<Proportion> coverage;
	
	public ClassMetrics(final Collection<? extends Item> children, BlockMetrics blockMetrics) {
		this.blockMetrics = blockMetrics;
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
		maximumComplexity = new LazyValue<Integer>() {
			protected Integer calculate() {
				return getCount() == 0 ? 0 : Collections.max(classes.value(), new ItemComplexityAscending()).getBlockMetrics().getComplexity();
			}
		};
		coverage = new LazyValue<Proportion>() {
			@Override
			protected Proportion calculate() {
				int part = 0;
				int whole = 0;
				for (ClassItem each : classes.value()) {
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
		return classes.value().size();
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

	public Proportion getCoverage() {
		return coverage.value();
	}
}
