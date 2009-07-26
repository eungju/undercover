package undercover.report;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import undercover.support.LazyValue;

public class ComplexityStatistics {
	private final LazyValue<? extends Collection<? extends Item>> items;
	
	public ComplexityStatistics(final LazyValue<? extends Collection<? extends Item>> items) {
		this.items = items;
	}

	private static final Comparator<Item> ASCENDING = new Comparator<Item>() {
		public int compare(Item a, Item b) {
			return a.getBlockMetrics().getComplexity() - b.getBlockMetrics().getComplexity();
		}
	};

	public int getMaximum() {
		return items.value().isEmpty() ? 0 : Collections.max(items.value(), ASCENDING).getBlockMetrics().getComplexity();
	}

	public double getAverage() {
		if (items.value().size() == 0) {
			return 0;
		}
		
		int result = 0;
		for (Item each : items.value()) {
			result += each.getBlockMetrics().getComplexity();
		}
		return result / (double) items.value().size();
	};
	
	public double getVariance() {
		if (items.value().size() == 0) {
			return 0;
		}

		Collection<? extends Item> elements = items.value();
		double avg = getAverage();
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
