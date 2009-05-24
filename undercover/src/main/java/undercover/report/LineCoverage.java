package undercover.report;

public class LineCoverage {
	public int blockCount = 0;
	public int coveredBlockCount = 0;
	public int touchCount = 0;

	public void addBlock(int blockCoverage) {
		blockCount++;
		if (blockCoverage > 0) {
			coveredBlockCount++;
			touchCount  = Math.max(touchCount, blockCoverage);
		}
	}
	
	public boolean isCompletelyCovered() {
		return blockCount == coveredBlockCount;
	}

	public boolean isPartialyCovered() {
		return coveredBlockCount > 0 && blockCount > coveredBlockCount;
	}
}