package undercover.report;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PackageItem extends PackageMeasure implements Item {
	private final String name;
	public final SortedSet<ClassItem> classes;
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	
	public PackageItem(String name) {
		this.name = name;
		classes = new TreeSet<ClassItem>(ClassItem.ORDER_BY_SIMPLE_NAME);
		complexity = new LazyComplexity(classes);
		blockCount = new LazyBlockCount(classes);
		coveredBlockCount = new LazyCoveredBlockCount(classes);
		methodCount = new LazyMethodCount(classes);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(classes);
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

	public static final Comparator<PackageItem> DISPLAY_ORDER = new Comparator<PackageItem>() {
		public int compare(PackageItem a, PackageItem b) {
			return a.name.compareTo(b.name);
		}
	};
}
