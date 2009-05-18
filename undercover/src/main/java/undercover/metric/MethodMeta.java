package undercover.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class MethodMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 4471359617355848867L;

	private String name;
	private List<BlockMeta> blocks;
	private int conditionalBranches;

	public MethodMeta(String name) {
		this.name = name;
		blocks = new ArrayList<BlockMeta>();
		conditionalBranches = 0;
	}
	
	public String name() {
		return name;
	}
	
	public void addBlock(BlockMeta blocksMeta) {
		blocks.add(blocksMeta);
	}
	
	public List<BlockMeta> blocks() {
		return blocks;
	}
	
	public void addConditionalBranch() {
		conditionalBranches++;
	}

	public void accept(MetaDataVisitor visitor) {
		visitor.visitEnter(this);
		for (BlockMeta each : blocks) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(name).append('{');
		builder.append('[');
		for (BlockMeta each : blocks) {
			builder.append(each.toString()).append(',');
		}
		builder.append(']');
		builder.append('}');
		return builder.toString();
	}

	public int getConditionalBranches() {
		return conditionalBranches;
	}
}
