package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;
import undercover.support.Proportion;

public class ClassMetrics {
	private final LazyValue<List<ClassItem>> classes;
	private final ComplexityStatistics complexity;
	private final LazyValue<Proportion> coverage;
	
	public ClassMetrics(final Collection<? extends Item> children) {
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
		complexity = new ComplexityStatistics(classes);
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
	
	public ComplexityStatistics getComplexity() {
		return complexity;
	}

	public Proportion getCoverage() {
		return coverage.value();
	}
}
