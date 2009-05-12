package undercover.metric;

import java.io.Serializable;
import java.util.UUID;

import undercover.support.ObjectSupport;

public class BlockMetric extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1085194971297677184L;

	private UUID id;

	public BlockMetric() {
		id = UUID.randomUUID();
	}
	
	public UUID id() {
		return id;
	}
	
	public String toString() {
		return "{}";
	}
}
