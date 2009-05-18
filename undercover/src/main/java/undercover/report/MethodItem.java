package undercover.report;

import undercover.metric.BlockMeta;
import undercover.metric.CoverageData;
import undercover.metric.MethodMeta;

public class MethodItem extends AbstractItem {
	private final ClassItem parent;
	private final int blockCount;
	private final int coveredBlockCount;
	private int complexity;
	
	public MethodItem(ClassItem parent, MethodMeta metric, CoverageData coverageData) {
		super(metric.name(), parent.getDisplayName() + "." + metric.name());
		this.parent = parent;
		this.blockCount = metric.blocks().size();
		this.complexity = 1 + metric.getConditionalBranches();
		int touched = 0;
		for (BlockMeta each : metric.blocks()) {
			if (coverageData.getBlock(each.id()) != null) {
				touched++;
			}
		}
		coveredBlockCount = touched;
	}
	
	public MethodItem(ClassItem parent, String name, String displayName, int blockCount, int coveredBlockCount) {
		super(name, displayName);
		this.parent = parent;
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
