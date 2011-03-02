package undercover.runtime;

import java.io.IOException;

public class Probe {
	public final static Probe INSTANCE = new Probe(UndercoverSettings.load());
	
	private final UndercoverSettings settings;
	private final CoverageData coverageData;
	private ExitHook exitHook;
	
	public Probe(UndercoverSettings settings) {
		this.settings = settings;
		coverageData = new CoverageData();
		installExitHook();
	}

    public UndercoverSettings getSettings() {
        return settings;
    }

    public void register(String className, int[][] coverage) {
        coverageData.register(className, coverage);
    }

	public CoverageData getCoverageData() {
		return coverageData;
	}

    public void saveCoverageData() {
        try {
            coverageData.save(settings.getCoverageFile());
        } catch (IOException e) {
            throw new RuntimeException("Unable to save coverage data to " + settings.getCoverageFile());
        }
    }

    public void onExit() {
        if (settings.isCoverageSaveOnExit()) {
            saveCoverageData();
        }
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
}
