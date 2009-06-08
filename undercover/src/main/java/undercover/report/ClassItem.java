package undercover.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassItem implements Item {
	private final String name;
	public final SourceFile sourceFile;
	public final List<Item> children;
	public final List<MethodItem> methods;
	public final List<ClassItem> classes;
	private final BlockMetrics blockMetrics;
	private final MethodMetrics methodMetrics;
	
	public ClassItem(String name, SourceFile sourceFile) {
		this.name = name;
		this.sourceFile = sourceFile;
		children = new ArrayList<Item>();
		methods = new ArrayList<MethodItem>();
		classes = new ArrayList<ClassItem>();
		blockMetrics = new BlockMetrics(children);
		methodMetrics = new MethodMetrics(children, blockMetrics);
	}
	
	public void addMethod(MethodItem methodItem) {
		children.add(methodItem);
		methods.add(methodItem);
	}

	public MethodItem getMethod(String methodName) {
		for (MethodItem each : methods) {
			System.out.println(each.getName() + "==" + methodName);
			if (each.getName().equals(methodName)) {
				return each;
			}
		}
		return null;
	}

	public void addClass(ClassItem classItem) {
		children.add(classItem);
		classes.add(classItem);
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

	public BlockMetrics getBlockMetrics() {
		return blockMetrics;
	}
	
	public MethodMetrics getMethodMetrics() {
		return methodMetrics;
	}
	
	public ClassMetrics getClassMetrics() {
		return null;
	}
	
	public PackageMetrics getPackageMetrics() {
		return null;
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
