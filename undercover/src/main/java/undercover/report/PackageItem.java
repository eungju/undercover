package undercover.report;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PackageItem implements Item {
	private final String name;
	public final SortedSet<ClassItem> classes;
	private BlockMetrics blockMetrics;
	private MethodMetrics methodMetrics;
	private ClassMetrics classMetrics;
	
	public PackageItem(String name) {
		this.name = name;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		blockMetrics = new BlockMetrics(classes);
		methodMetrics = new MethodMetrics(classes, blockMetrics);
		classMetrics = new ClassMetrics(classes, blockMetrics);
	}
	
	public void addClass(ClassItem child) {
		classes.add(child);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return name.replaceAll("/", ".");
	}
	
	public String getLinkName() {
		return name.replaceAll("/", ".");
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
		return null;
	}

	public static final Comparator<PackageItem> DISPLAY_ORDER = new Comparator<PackageItem>() {
		public int compare(PackageItem a, PackageItem b) {
			return a.name.compareTo(b.name);
		}
	};
}
