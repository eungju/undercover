package undercover.report;

public class MethodItem extends AbstractItem {
	private final int complexity;
	private final int blockCount;
	private final int coveredBlockCount;
	
	public MethodItem(ClassItem parent, String name, int complexity, int blockCount, int coveredBlockCount) {
		super(name, parent.getDisplayName() + "." + name);
		this.complexity = complexity;
		this.blockCount = blockCount;
		this.coveredBlockCount = coveredBlockCount;
	}

	public int getBlockCount() {
		return blockCount;
	}

	public int getComplexity() {
		return complexity;
	}

	public int getMethodCount() {
		throw new UnsupportedOperationException();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount;
	}
}
