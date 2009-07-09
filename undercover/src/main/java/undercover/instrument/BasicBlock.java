package undercover.instrument;

import java.util.HashSet;
import java.util.Set;

import undercover.support.ObjectSupport;

public class BasicBlock extends ObjectSupport {
	/** Inclusive */
	public int start;
	/** Exclusive */
	public int end;
	public int conditionals;
	public Set<Integer> lines;
	
	public BasicBlock(int startOffset) {
		this(startOffset, 0, 0, new HashSet<Integer>());
	}
	
	public BasicBlock(int startOffset, int endOffset, int conditionals, Set<Integer> lines) {
		this.start = startOffset;
		this.end = endOffset;
		this.conditionals = conditionals;
		this.lines = lines;
	}
}
