package undercover.instrument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class InstrumentApp {
	public static void main(String[] args) throws IOException {
		InstrumentApp app = new InstrumentApp();
		app.arguments(args);
		System.exit(app.execute());
	}

	private File inputDir;
	private File outputDir;
	
	public void arguments(String[] args) {
		inputDir = new File(args[0]);
		outputDir = new File(args[1]);
	}

	public int execute() {
		try {
			instrumentDir(inputDir, outputDir);
			return 0;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return 255; 
	}
	
	public void instrumentDir(File inputDir, File outputDir) throws IOException {
		outputDir.mkdir();
		for (File each : inputDir.listFiles()) {
			if (each.isDirectory()) {
				instrumentDir(each, new File(outputDir, each.getName()));
			} else if (each.getName().endsWith(".class")) {
				instrumentFile(each, new File(outputDir, each.getName()));
			}
		}
	}

	public void instrumentFile(File inputFile, File outputFile) throws IOException {
		Instrument instrument = new Instrument();
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
