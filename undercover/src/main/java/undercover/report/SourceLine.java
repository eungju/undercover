package undercover.report;

public class SourceLine {
	public final int number;
	public final String text;
	public int blockCount = 0;
	public int coveredBlockCount = 0;
	public int touchCount = 0;
	
	public SourceLine(int number, String text) {
		this.number = number;
		this.text = text;
	}
	
	public void addBlock(int executionCount) {
		blockCount++;
		if (executionCount > 0) {
			coveredBlockCount++;
			touchCount = Math.max(touchCount, executionCount);
		}
	}
	
	public boolean isExecutable() {
		return blockCount > 0;
	}

	public boolean isCompletelyCovered() {
		return blockCount == coveredBlockCount;
	}

	public boolean isPartialyCovered() {
		return coveredBlockCount > 0 && blockCount > coveredBlockCount;
	}
}