package undercover.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

import undercover.instrument.OfflineInstrument;

public class InstrumentTask extends UndercoverTask {
	private Path instrumentPath;
	private File destDir;

	public void addInstrumentPath(Path path) {
		instrumentPath = path;
	}

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}
	
	public void execute() throws BuildException {
        log("Instrumenting...");

        if (metaDataFile == null) {
			metaDataFile = new File(destDir, "undercover.md");
		}
		if (coverageDataFile == null) {
			coverageDataFile = new File(destDir, "undercover.cd");
		}

		List<File> instrumentPaths = new ArrayList<File>();
		for (String each : (String[]) instrumentPath.list()) {
			log("Add instrument path: " + each);
			instrumentPaths.add(new File(each));
		}
		
    	OfflineInstrument instrument = new OfflineInstrument();
    	try {
    		instrument.setInputPaths(instrumentPaths.toArray(new File[instrumentPaths.size()]));
    		instrument.setOutputDirectory(new File(destDir, "classes"));
    		instrument.setMetaDataFile(metaDataFile);
    		instrument.setCoverageDataFile(coverageDataFile);
			instrument.run();
		} catch (Exception e) {
			throw new BuildException(e);
		}
    }
}
