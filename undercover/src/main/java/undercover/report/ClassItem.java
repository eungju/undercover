package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.metric.ClassMetric;

public class ClassItem extends CompositeItem {
	public final PackageItem parent;
	public final String sourcePath;
	public final String simpleName;
	public final List<MethodItem> methods;
	
	public ClassItem(PackageItem parent, String name, String sourcePath) {
		super(name, name.replaceAll("\\/", "."));
		this.parent = parent;
		this.sourcePath = sourcePath;
		this.simpleName = name.substring(name.lastIndexOf('/') + 1);
		this.methods = new ArrayList<MethodItem>();
	}
	
	public String getLink() {
		return sourcePath.replaceAll("/", ".") + ".html";
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
}
