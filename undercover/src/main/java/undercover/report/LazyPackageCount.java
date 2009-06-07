package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public class LazyPackageCount extends LazyValue<Integer> {
	private Collection<PackageMeasure> children;

	public LazyPackageCount(Collection<PackageMeasure> children) {
		this.children = children;
	}
	
	protected Integer calculate() {
		return children.size();
	}
}