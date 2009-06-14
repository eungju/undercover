package undercover.report;

import java.util.SortedSet;
import java.util.TreeSet;

public class ProjectItem implements Item {
	private final String displayName;
	public final SortedSet<PackageItem> packages;
	private BlockMetrics blockMetrics;
	private MethodMetrics methodMetrics;
	private ClassMetrics classMetrics;
	private PackageMetrics packageMetrics;
	
	public ProjectItem(String displayName) {
		this.displayName = displayName;
		packages = new TreeSet<PackageItem>(PackageItem.DISPLAY_ORDER);
		blockMetrics = new BlockMetrics(packages);
		methodMetrics = new MethodMetrics(packages, blockMetrics);
		classMetrics = new ClassMetrics(packages, blockMetrics);
		packageMetrics = new PackageMetrics(packages, blockMetrics);
	}
	
	public void addPackage(PackageItem packageItem) {
		packages.add(packageItem);
	}

	public String getDisplayName() {
		return displayName;
	}

	public BlockMetrics getBlockMetrics() {
		return blockMetrics;
	}

	public MethodMetrics getMethodMetrics() {
		return methodMetrics;
	}

	public ClassMetrics getClassMetrics() {
		return classMetrics;
	}
	
	public PackageMetrics getPackageMetrics() {
		return packageMetrics;
	}
}
