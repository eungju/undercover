package undercover.report;

import java.util.SortedSet;
import java.util.TreeSet;

public class ProjectItem extends ProjectMeasure {
	private final String displayName;
	public final SortedSet<PackageItem> packages;
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	
	public ProjectItem(String displayName) {
		this.displayName = displayName;
		packages = new TreeSet<PackageItem>(PackageItem.DISPLAY_ORDER);
		complexity = new LazyComplexity(packages);
		blockCount = new LazyBlockCount(packages);
		coveredBlockCount = new LazyCoveredBlockCount(packages);
		methodCount = new LazyMethodCount(packages);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(packages);
	}
	
	public void addPackage(PackageItem packageItem) {
		packages.add(packageItem);
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getLinkName() {
		throw new UnsupportedOperationException();
	}

	public int getComplexity() {
		return complexity.value();
	}

	public int getBlockCount() {
		return blockCount.value();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount.value();
	}
	
	public int getMethodCount() {
		return methodCount.value();
	}
	
	public int getMaximumMethodComplexity() {
		return maximumMethodComplexity.value();
	}
}
