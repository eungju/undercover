package undercover.instrument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import undercover.runtime.UndercoverSettings;

public class OfflineInstrument {
	private Instrument instrument;
	private File[] inputPaths;
	private File outputDirectory;
	private File metaDataFile;
	private File coverageDataFile;
	
	public void setInputPaths(File[] inputPaths) {
		this.inputPaths = inputPaths;
	}
	
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}

	public void setCoverageDataFile(File coverageDataFile) {
		this.coverageDataFile = coverageDataFile;
	}
	
	public void run() throws Exception {
		instrument = new Instrument();
		instrumentDirs(inputPaths, outputDirectory);
		instrument.getMetaData().save(metaDataFile);
    	
		UndercoverSettings settings = new UndercoverSettings();
		settings.setCoverageSaveOnExit(true);
		settings.setCoverageFile(coverageDataFile);
		settings.save(new File(outputDirectory, "undercover.properties"));
	}
	
	public void instrumentDirs(File[] inputDirs, File outputDir) throws IOException {
		outputDir.mkdirs();
		for (File each : inputDirs) {
			instrumentDir(each, outputDir);
		}
	}
	
	public void instrumentDir(File inputDir, File outputDir) throws IOException {
		outputDir.mkdir();
		for (File each : inputDir.listFiles()) {
			if (each.isDirectory()) {
				instrumentDir(each, new File(outputDir, each.getName()));
			} else if (each.getName().endsWith(".class")) {
				instrumentFile(each, new File(outputDir, each.getName()));
			} else {
				copyFile(each, new File(outputDir, each.getName()));
			}
		}
	}

	void copyFile(File inputFile, File outputFile) throws IOException {
		FileUtils.copyFile(inputFile, outputFile);
	}

	public void instrumentFile(File inputFile, File outputFile) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(inputFile);
			output = new FileOutputStream(outputFile);
			output.write(instrument.instrument(input));
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}
	}
}
