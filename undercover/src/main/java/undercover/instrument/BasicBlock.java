package undercover.instrument;

import java.util.HashSet;
import java.util.Set;

import undercover.support.ObjectSupport;

public class BasicBlock extends ObjectSupport {
	/** Inclusive */
	public int start;
	/** Exclusive */
	public int end;
	public Set<Integer> successors;
	
	public BasicBlock(int startOffset) {
		this(startOffset, 0, new HashSet<Integer>());
	}
	
	public BasicBlock(int startOffset, int endOffset, Set<Integer> successors) {
		this.start = startOffset;
		this.end = endOffset;
		this.successors = successors;
	}

	public void addSuccessor(int successor) {
		successors.add(successor);
	}
}
