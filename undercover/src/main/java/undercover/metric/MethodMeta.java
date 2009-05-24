package undercover.metric;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import undercover.support.ObjectSupport;

public class MethodMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 4471359617355848867L;

	public final String name;
	public final int complexity;
	public final List<BlockMeta> blocks;
	
	public MethodMeta(String name, int complexity) {
		this(name, complexity, Collections.<BlockMeta>emptyList());
	}

	public MethodMeta(String name, int complexity, List<BlockMeta> blocks) {
		this.name = name;
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
