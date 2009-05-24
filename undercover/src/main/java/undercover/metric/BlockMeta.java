package undercover.metric;

import java.io.Serializable;
import java.util.Collection;

import undercover.support.ObjectSupport;

public class BlockMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -1085194971297677184L;

	public final Collection<Integer> lines;
	
	public BlockMeta(Collection<Integer> lines) {
		this.lines = lines;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visit(this);
	}
}
