package undercover.instrument;

import undercover.support.ObjectSupport;

public class BasicBlock extends ObjectSupport {
	/** Inclusive */
	public int startOffset;
	/** Inclusive */
	public int endOffset;

	public BasicBlock(int startOffset) {
		this.startOffset = startOffset;
	}
	
	public BasicBlock(int startOffset, int endOffset) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

}
