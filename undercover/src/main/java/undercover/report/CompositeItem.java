package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public abstract class CompositeItem extends AbstractItem {
	private LazyValue<Integer> blockCount = new LazyValue<Integer>() {
		protected Integer calculate() {
			int result = 0;
			for (Item each : getItems()) {
				result += each.getBlockCount();
			}
			return result;
		}
	};
	
	private LazyValue<Integer> coveredBlockCount = new LazyValue<Integer>() {
		protected Integer calculate() {
			int result = 0;
			for (Item each : getItems()) {
				result += each.getCoveredBlockCount();
			}
			return result;
		}
	};
	
	private LazyValue<Integer> complexity = new LazyValue<Integer>() {
		protected Integer calculate() {
			int result = 0;
			for (Item each : getItems()) {
				result += each.getComplexity();
			}
			return result;
		}
	};
	
	private LazyValue<Integer> methodCount = new LazyValue<Integer>() {
		protected Integer calculate() {
			int result = 0;
			for (Item each : getItems()) {
				result += each.getMethodCount();
			}
			return result;
		}
	};
	
	public CompositeItem(String name, String displayName) {
		super(name, displayName);
	}

	public int getBlockCount() {
		return blockCount.value();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount.value();
	}

	public int getComplexity() {
		return complexity.value();
	}

	public int getMethodCount() {
		return methodCount.value();
	}
	
	protected abstract Collection<Item> getItems();
}
