package undercover.report;

public abstract class AbstractItem implements Item {
	protected final String name;
	protected final String displayName;

	public AbstractItem(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public double getAverageMethodComplexity() {
		return ((double) getBlockCount()) / getMethodCount();
	}

	public double getCoverageRate() {
		return getBlockCount() == 0 ? 1 : ((double) getCoveredBlockCount()) / getBlockCount();
	}
}
