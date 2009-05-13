package undercover.report;

import java.util.ArrayList;
import java.util.List;

public class PackageItem {
	public String name;
	public String displayName;
	public List<ClassItem> children;
	
	public PackageItem(String name) {
		this.name = name;
		this.displayName = name.replaceAll("\\/", ".");
		this.children = new ArrayList<ClassItem>();
	}
	
	public void addChild(ClassItem child) {
		children.add(child);
	}
	
	public int getComplexity() {
		return 0;
	}
	
	public float getCoverageRate() {
		return 0;
	}
}
