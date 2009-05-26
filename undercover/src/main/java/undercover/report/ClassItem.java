package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ClassItem extends CompositeItem {
	public final SourceFile sourceFile;
	public final String simpleName;
	public final List<MethodItem> methods;
	
	public ClassItem(String name, SourceFile sourceFile) {
		super(name, name.replaceAll("/", "."));
		this.sourceFile = sourceFile;
		this.simpleName = getSimpleName(name);
		this.methods = new ArrayList<MethodItem>();
	}
	
	public String getLink() {
		return "source-" + sourceFile.path.replaceAll("/", ".") + ".html";
	}

	public void addMethod(MethodItem methodItem) {
		methods.add(methodItem);
	}
	
	protected Collection<Item> getItems() {
		return (Collection) methods;
	}
	
	public int getMethodCount() {
		return methods.size();
	}

	static String getSimpleName(String className) {
		int index = className.lastIndexOf('/');
		return index == -1 ? className : className.substring(index + 1);
	}
	
	public static final Comparator<ClassItem> ORDER_BY_SIMPLE_NAME = new Comparator<ClassItem>() {
		public int compare(ClassItem a, ClassItem b) {
			return a.simpleName.compareTo(b.simpleName);
		}
	};

	public static final Comparator<String> NAME_ORDER_BY_SIMPLE_NAME = new Comparator<String>() {
		public int compare(String a, String b) {
			return getSimpleName(a).compareTo(getSimpleName(b));
		}
	};
}
