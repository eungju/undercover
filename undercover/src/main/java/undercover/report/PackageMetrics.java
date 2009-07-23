package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.support.LazyValue;

public class PackageMetrics  {
	private final LazyValue<List<PackageItem>> packages;
	private final ComplexityStatistics complexity;

	public PackageMetrics(final Collection<? extends Item> children) {
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
		complexity = new ComplexityStatistics(packages);
	}

	public int getCount() {
		return packages.value().size();
	}

	public ComplexityStatistics getComplexity() {
		return complexity;
	}
}
