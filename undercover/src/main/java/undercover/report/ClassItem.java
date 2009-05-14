package undercover.report;

import java.util.ArrayList;
import java.util.List;

import undercover.metric.ClassMetric;

public class ClassItem extends AbstractItem {
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

	public int getBlockCount() {
		int result = 0;
		for (Item each : methods) {
			result += each.getBlockCount();
		}
		return result;
	}

	public int getCoveredBlockCount() {
		int result = 0;
		for (Item each : methods) {
			result += each.getCoveredBlockCount();
		}
		return result;
	}

	public int getMethodCount() {
		return methods.size();
	}
}
