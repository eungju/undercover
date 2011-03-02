package undercover.runtime;

public class ExitHook extends Thread {
	private final Probe probe;

	public ExitHook(Probe probe) {
		this.probe = probe;
	}
	
	public void run() {
        probe.onExit();
	}
}