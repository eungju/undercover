package undercover.runtime;

public abstract class Probe {
	private Probe() {
	}
	
	public static void touchBlock(int blockId) {
		System.out.println("Touch block " + blockId);
	}
}
