package undercover.metric;

public class FragmentMetric {
	private int line;
	private int branchCount;
	private int touchCount;

	public FragmentMetric(int line) {
		this.line = line;
		branchCount = 0;
		touchCount = 0;
	}
	
	public int line() {
		return line;
	}

	public void foundBranch() {
		branchCount++;
	}

	public void touch() {
		touchCount++;
	}
	
	public String toString() {
		return "{" + line + "," + branchCount + "}";
	}
}
