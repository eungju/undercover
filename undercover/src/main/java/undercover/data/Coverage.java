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
}
