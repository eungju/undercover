package undercover.metric;

import java.io.Serializable;

import undercover.support.ObjectSupport;

public class BlockCoverage extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 5869498470093075986L;
	private int touchCount;

	public BlockCoverage() {
		this(0);
	}
	
	public BlockCoverage(int touchCount) {
		this.touchCount = touchCount;
	}
	
	public void touch() {
		touchCount++;
	}
	
	public int touchCount() {
		return touchCount;
	}
	
	public boolean isTouched() {
		return touchCount > 0;
	}
}
