package undercover.report;

import java.util.Collection;

public abstract class CompositeItem extends AbstractItem {
	public CompositeItem(String name, String displayName) {
		super(name, displayName);
	}

	public int getBlockCount() {
		int result = 0;
		for (Item each : getItems()) {
			result += each.getBlockCount();
		}
		return result;
	}

	public int getCoveredBlockCount() {
		int result = 0;
		for (Item each : getItems()) {
			result += each.getCoveredBlockCount();
		}
		return result;
	}

	public int getComplexity() {
		int result = 0;
		for (Item each : getItems()) {
			result += each.getComplexity();
		}
		return result;
	}

	public int getMethodCount() {
		int result = 0;
		for (Item each : getItems()) {
			result += each.getMethodCount();
		}
		return result;
	}
	
	protected abstract Collection<Item> getItems();
}
