package undercover.instrument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import undercover.instrument.filter.GlobFilter;
import undercover.support.FileUtils;
import undercover.support.IOUtils;
import undercover.support.JdkLogger;
import undercover.support.Logger;

public class OfflineInstrument {
	private Logger logger = new JdkLogger();
	private Instrument instrument;
	private List<File> instrumentPaths;
	private File outputDirectory;
	private File metaDataFile;
	private GlobFilter filter;
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setInstrumentPaths(List<File> instrumentPaths) {
		this.instrumentPaths = instrumentPaths;
	}
	
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}
	
	public void setFilter(GlobFilter filter) {
		this.filter = filter;
	}

	public void fullcopy() throws Exception {
		logger.info("Instrument paths: " + instrumentPaths);
		logger.info("Output directory: " + outputDirectory);
		
		File classesDir = new File(outputDirectory, "classes");
		instrument = new Instrument();
		instrument.addFilter(filter);
		instrumentDirs(instrumentPaths, classesDir);
		instrument.getMetaData().save(metaDataFile);
	}
	
	void instrumentDirs(List<File> inputDirs, File outputDir) throws IOException {
		outputDir.mkdirs();
		for (File each : inputDirs) {
			instrumentDir(each, outputDir);
		}
	}
	
	void instrumentDir(File inputDir, File outputDir) throws IOException {
		outputDir.mkdir();
		for (File each : inputDir.listFiles()) {
			String name = each.getName();
			if (each.isDirectory()) {
				instrumentDir(each, new File(outputDir, each.getName()));
			} else if (name.endsWith(".class")) {
				instrumentFile(each, new File(outputDir, each.getName()));
			} else {
				copyFile(each, new File(outputDir, each.getName()));
			}
		}
	}

	void copyFile(File inputFile, File outputFile) throws IOException {
		logger.debug("Copying file " + inputFile);
		FileUtils.copyFile(inputFile, outputFile);
	}

	void instrumentFile(File inputFile, File outputFile) throws IOException {
		logger.debug("Instrumenting file " + inputFile);
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
