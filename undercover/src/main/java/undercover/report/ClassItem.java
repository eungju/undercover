package undercover.report;

import undercover.metric.ClassMetric;

public class ClassItem {
	public PackageItem parent;
	public String name;
	public String displayName;
	public String simpleName;
	
	public ClassItem(PackageItem parent, ClassMetric metric) {
		this.parent = parent;
		this.name = metric.name();
		this.displayName = name.replaceAll("\\/", ".");
		this.simpleName = name.substring(0, name.lastIndexOf('/'));
	}
	
	public float getCoverageRate() {
		return 0;
	}
}
