package undercover.runtime;

import java.io.IOException;

public class ExitHook extends Thread {
	private Probe probe;
	private UndercoverSettings settings;
	
	public ExitHook(Probe probe) {
		this.probe = probe;
		this.settings = probe.getSettings();
	}
	
	public void run() {
		if (settings.isCoverageSaveOnExit()) {
			try {
				CoverageData coverageData = probe.getCoverageData();
				coverageData.save(settings.getCoverageFile());
			} catch (IOException e) {
				throw new RuntimeException("Unable to save coverage data to " + settings.getCoverageFile());
			}
		}
	}
}