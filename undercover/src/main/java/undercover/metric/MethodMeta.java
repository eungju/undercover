package undercover.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class MethodMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = 4471359617355848867L;

	private String name;
	private List<BlockMeta> blocks;
	private int complexity;
	
	public MethodMeta(String name, int complexity) {
		this.name = name;
		blocks = new ArrayList<BlockMeta>();
		this.complexity = complexity;
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

	public void accept(MetaDataVisitor visitor) {
		visitor.visitEnter(this);
		for (BlockMeta each : blocks) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public int getComplexity() {
		return complexity;
	}
}
