package undercover.metric;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import undercover.support.ObjectSupport;

public class BlockMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1085194971297677184L;

	private final UUID id;
	private final Collection<Integer> lines;
	
	public BlockMeta(Collection<Integer> lines) {
		id = UUID.randomUUID();
		this.lines = lines;
	}
	
	public UUID id() {
		return id;
	}
	
	public Collection<Integer> lines() {
		return lines;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visit(this);
	}
}
