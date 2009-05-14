package undercover.report;

import java.util.ArrayList;
import java.util.List;

public class PackageItem extends AbstractItem {
	public final List<ClassItem> classes;
	
	public PackageItem(String name) {
		super(name, name.replaceAll("\\/", "."));
		this.classes = new ArrayList<ClassItem>();
	}
	
	public void addClass(ClassItem child) {
		classes.add(child);
	}

	public int getBlockCount() {
		int result = 0;
		for (ClassItem each : classes) {
			result += each.getBlockCount();
		}
		return result;
	}

	public int getCoveredBlockCount() {
		int result = 0;
		for (ClassItem each : classes) {
			result += each.getCoveredBlockCount();
		}
		return result;
	}

	public int getMethodCount() {
		int result = 0;
		for (ClassItem each : classes) {
			result += each.getMethodCount();
		}
		return result;
	}
}
