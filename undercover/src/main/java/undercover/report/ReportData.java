package undercover.report;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ReportData implements Item {
	private final String displayName;
	private final SortedSet<PackageItem> packages;
	private final SortedSet<ClassItem> classes;
	private final SortedSet<SourceItem> sources;

	private final BlockMetrics blockMetrics;
	private final MethodMetrics methodMetrics;
	private final ClassMetrics classMetrics;
	private final PackageMetrics packageMetrics;
	
	public ReportData(String displayName, Collection<PackageItem> packageItems, Collection<ClassItem> classItems, Collection<SourceItem> sourceItems) {
		this.displayName = displayName;
		this.packages = new TreeSet<PackageItem>(PackageItem.DISPLAY_ORDER);
		this.packages.addAll(packageItems);
		this.classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		this.classes.addAll(classItems);
		this.sources = new TreeSet<SourceItem>(new Comparator<SourceItem>() {
			public int compare(SourceItem a, SourceItem b) {
				return a.getName().compareTo(b.getName());
			}
		});
		this.sources.addAll(sourceItems);
		
		blockMetrics = new BlockMetrics(packages);
		methodMetrics = new MethodMetrics(packages, blockMetrics);
		classMetrics = new ClassMetrics(packages, blockMetrics);
		packageMetrics = new PackageMetrics(packages, blockMetrics);
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public Collection<PackageItem> getPackages() {
		return packages;
	}
	
	public Collection<ClassItem> getClasses() {
		return classes;
	}
	
	public Collection<SourceItem> getSources() {
		return sources;
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
