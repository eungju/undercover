package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;
import undercover.support.Proportion;

public class MethodMetrics {
	private final LazyValue<List<MethodItem>> methods;
	private final ComplexityStatistics complexity;
	private final LazyValue<Proportion> coverage;

	public MethodMetrics(final Collection<? extends Item> children) {
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
		complexity = new ComplexityStatistics(methods);
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
	
	public ComplexityStatistics getComplexity() {
		return complexity;
	}
	
	public Proportion getCoverage() {
		return coverage.value();
	}
}
