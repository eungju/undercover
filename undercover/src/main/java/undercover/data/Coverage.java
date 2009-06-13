package undercover.data;

import java.io.Serializable;

public class Coverage implements Serializable {
	private static final long serialVersionUID = -6522224503868018324L;
	public final String className;
	public final int[][] blocks;
	
	public Coverage(String className, int[][] blocks) {
		this.className = className;
		this.blocks = blocks;
	}
	
	public int countExecution(int methodIndex, int blockIndex) {
		return blocks[methodIndex][blockIndex];
	}

	public int countCoveredBlocks(int methodIndex) {
		int result = 0;
		for (int each : blocks[methodIndex]) {
			if (each > 0) {
				result++;
			}
		}
		return result;
	}
}
