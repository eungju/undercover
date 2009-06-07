package undercover.report;

import java.util.Collection;

public abstract class ProjectMeasure extends PackageMeasure {
	private LazyPackageCount packageCount;

	public void initializeProjectMeasure(Collection<PackageMeasure> children) {
		super.initializePackageMeasure(children);
		packageCount = new LazyPackageCount(children);
	}
	
	public int getPackageCount() {
		return packageCount.value();
	}

	public double getAveragePackageComplexity() {
		return ((double) getComplexity()) / getPackageCount();
	};
}
