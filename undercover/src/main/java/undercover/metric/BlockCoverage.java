package undercover.metric;

import java.io.Serializable;
import java.util.UUID;

import undercover.support.ObjectSupport;

public class BlockCoverage extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 5869498470093075986L;
	private UUID id;
	private int touchCount;

	public BlockCoverage(UUID id) {
		this.id = id;
	}
	
	public void touch() {
		touchCount++;
	}
	
	public int touchCount() {
		return touchCount;
	}
}
