package undercover.ant;

import java.io.File;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import undercover.instrument.OfflineInstrument;

public class InstrumentTask extends Task {
	private File destDir;
	private File metaDataFile;
	private Path instrumentPath;

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}

	public void addInstrumentPath(Path path) {
		instrumentPath = path;
	}
	
	public void execute() throws BuildException {
		for (String each : (String[]) instrumentPath.list()) {
			System.out.println("instrument: " + each);
		}
		
    	OfflineInstrument instrument = new OfflineInstrument();
    	try {
    		//instrument.setInputPaths(inputPaths)
    		instrument.setOutputDirectory(destDir);
    		instrument.setMetaDataFile(metaDataFile);
			//instrument.run();
		} catch (Exception e) {
			throw new BuildException(e);
		}
        log("I'm instrumenter.");
    }
}
