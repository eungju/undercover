package undercover.report;

import undercover.metric.BlockCoverage;

public class LineCoverage {
	public int blockCount = 0;
	public int coveredBlockCount = 0;

	public void addBlock(BlockCoverage blockCoverage) {
		blockCount++;
		if (blockCoverage != null && blockCoverage.touchCount() > 0) {
			coveredBlockCount++;
		}
	}
	
	public boolean isCompletelyCovered() {
		return blockCount == coveredBlockCount;
	}

	public boolean isPartialyCovered() {
		return coveredBlockCount > 0 && blockCount > coveredBlockCount;
	}
}