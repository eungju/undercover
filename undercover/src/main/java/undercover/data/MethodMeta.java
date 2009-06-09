package undercover.data;

import java.io.Serializable;
import java.util.List;

import undercover.support.ObjectSupport;

public class MethodMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 4471359617355848867L;

	public final String name;
	public final String descriptor;
	public final int complexity;
	public final List<BlockMeta> blocks;
	
	public MethodMeta(String name, String descriptor, int complexity, List<BlockMeta> blocks) {
		this.name = name;
		this.descriptor = descriptor;
		this.complexity = complexity;
		this.blocks = blocks;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visitEnter(this);
		for (BlockMeta each : blocks) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}
}
