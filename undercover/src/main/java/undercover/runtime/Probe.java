package undercover.runtime;

import undercover.UndercoverSettings;
import undercover.data.CoverageData;

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
	
	public CoverageData getCoverageData() {
		return coverageData;
	}

	public void register(String className, int[][] coverage) {
		coverageData.register(className, coverage);
	}
}
