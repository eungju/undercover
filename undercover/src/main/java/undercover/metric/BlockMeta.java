package undercover.metric;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import undercover.support.ObjectSupport;

public class BlockMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1085194971297677184L;

	private final UUID id;
	private final Set<Integer> lines;
	
	public BlockMeta() {
		id = UUID.randomUUID();
		lines = new HashSet<Integer>();
	}
	
	public UUID id() {
		return id;
	}
	
	public void addLine(int lineNumber) {
		lines.add(lineNumber);
	}
	
	public Set<Integer> lines() {
		return lines;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return "{}";
	}
}
