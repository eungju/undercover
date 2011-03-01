package undercover.runtime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class UndercoverSettings {
	private static final String LOCATION = "undercover.properties";
	private static final String COVERAGEDATA_SAVE_ON_EXIT = "undercover.coveragedata.saveOnExit";
	private static final String COVERAGEDATA_FILE = "undercover.coveragedata.file";
	
	private final Properties properties;

	public static UndercoverSettings load() {
		Properties systemProperties = System.getProperties();
		Properties properties = new Properties(systemProperties);
		InputStream input = null;
		try {
			input = UndercoverSettings.class.getClassLoader().getResourceAsStream(LOCATION);
			if (input != null) {
				properties.load(input);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load properties from " + LOCATION, e);
		} finally {
            Helper.closeQuietly(input);
		}
		return new UndercoverSettings(properties);
	}
	
	public UndercoverSettings() {
		this(new Properties());
	}
	
	public UndercoverSettings(Properties properties) {
		this.properties = properties;
	}
	
	public void save(File file) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			properties.store(output, "Undercover settings");
		} finally {
            Helper.closeQuietly(output);
		}
	}
	
	public boolean getProperty(String key, boolean defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return defaultValue;
	}

	public File getProperty(String key, File defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return new File(value);
		}
		return defaultValue;
	}

	public boolean isCoverageSaveOnExit() {
		return getProperty(COVERAGEDATA_SAVE_ON_EXIT, false);
	}
	
	public void setCoverageSaveOnExit(boolean coverageSaveOnExit) {
		properties.setProperty(COVERAGEDATA_SAVE_ON_EXIT, Boolean.toString(coverageSaveOnExit));
	}
	
	public File getCoverageFile() {
		return getProperty(COVERAGEDATA_FILE, null);
	}

	public void setCoverageFile(File file) {
		properties.setProperty(COVERAGEDATA_FILE, file.getAbsolutePath());
	}
}
