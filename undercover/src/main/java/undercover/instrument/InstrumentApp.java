package undercover.instrument;

import java.io.File;
import java.io.IOException;

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
		Instrument instrument = new Instrument();
		instrument.instrumentDir(inputDir, outputDir);
		return 0; 
	}
}
