package undercover.metric;

import java.io.Serializable;
import java.util.UUID;

import undercover.support.ObjectSupport;

public class BlockMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1085194971297677184L;

	private UUID id;

	public BlockMeta() {
		id = UUID.randomUUID();
	}
	
	public UUID id() {
		return id;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return "{}";
	}
}
