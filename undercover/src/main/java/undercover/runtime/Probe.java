package undercover.runtime;

import java.util.UUID;

import undercover.UndercoverSettings;
import undercover.metric.CoverageData;

public class Probe {
	public final static Probe INSTANCE = new Probe(UndercoverSettings.load());
	
	private UndercoverSettings settings;
	private CoverageData coverageData;
	private ExitHook exitHook;
	
	public Probe(UndercoverSettings settings) {
		this.settings = settings;
		coverageData = new CoverageData();
		installExitHook();
	}
	
	public UndercoverSettings getSettings() {
		return settings;
	}
	
	public void installExitHook() {
		exitHook = new ExitHook(this);
		Runtime.getRuntime().addShutdownHook(exitHook);
	}
	
	public void uninstallExitHook() {
		if (exitHook != null) {
			Runtime.getRuntime().removeShutdownHook(exitHook);
		}
	}
	
	public synchronized void touchBlock(String blockId) {
		System.out.println("Touch block " + blockId);
		coverageData.touchBlock(UUID.fromString(blockId));
	}
	
	public synchronized CoverageData getCoverageData() {
		return coverageData;
	}
}
