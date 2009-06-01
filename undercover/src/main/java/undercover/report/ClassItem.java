package undercover.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassItem extends ClassMeasure implements Item {
	private final String name;
	public final SourceFile sourceFile;
	public final List<MethodItem> methods;
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;
	private final LazyMethodCount methodCount;
	private final LazyMaximumMethodComplexity maximumMethodComplexity;
	
	public ClassItem(String name, SourceFile sourceFile) {
		this.name = name;
		this.sourceFile = sourceFile;
		methods = new ArrayList<MethodItem>();
		complexity = new LazyComplexity(methods);
		blockCount = new LazyBlockCount(methods);
		coveredBlockCount = new LazyCoveredBlockCount(methods);
		methodCount = new LazyMethodCount(methods);
		maximumMethodComplexity = new LazyMaximumMethodComplexity(methods);
	}
	
	public void addMethod(MethodItem methodItem) {
		methods.add(methodItem);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return name.replaceAll("/", ".");
	}

	static String getSimpleName(String className) {
		int index = className.lastIndexOf('/');
		return index == -1 ? className : className.substring(index + 1);
	}

	public String getSimpleName() {
		return getSimpleName(name);
	}
	
	public String getLinkName() {
		return sourceFile.path.replaceAll("/", ".");
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
	
	public static final Comparator<ClassItem> ORDER_BY_SIMPLE_NAME = new Comparator<ClassItem>() {
		public int compare(ClassItem a, ClassItem b) {
			return a.getSimpleName().compareTo(b.getSimpleName());
		}
	};

	public static final Comparator<String> NAME_ORDER_BY_SIMPLE_NAME = new Comparator<String>() {
		public int compare(String a, String b) {
			return getSimpleName(a).compareTo(getSimpleName(b));
		}
	};
}
