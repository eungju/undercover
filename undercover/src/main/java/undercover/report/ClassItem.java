package undercover.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import undercover.metric.ClassMetric;

public class ClassItem extends CompositeItem {
	public final PackageItem parent;
	public final String simpleName;
	public final List<MethodItem> methods;
	
	public ClassItem(PackageItem parent, ClassMetric metric) {
		this(parent, metric.name());
	}
	
	public ClassItem(PackageItem parent, String name) {
		super(name, name.replaceAll("\\/", "."));
		this.parent = parent;
		this.simpleName = name.substring(name.lastIndexOf('/') + 1);
		this.methods = new ArrayList<MethodItem>();
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
