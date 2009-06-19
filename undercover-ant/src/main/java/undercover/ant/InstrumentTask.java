package undercover.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import undercover.instrument.OfflineInstrument;

public class InstrumentTask extends UndercoverTask {
	Path instrumentPath;
	File destDir;
	
	OfflineInstrument instrument;
	List<File> instrumentPaths;
	
	/**
	 * instrumentpath attribute.
	 */
	public void setInstrumentPath(Path path) {
		if (instrumentPath == null)
			instrumentPath = path;
		else
			instrumentPath.append(path);
	}

	/**
	 * instrumentpathref attribute.
	 */
	public void setInstrumentPathRef(Reference ref) {
		createInstrumentPath().setRefid(ref);
	}

	/**
	 * instrumentpath element.
	 */
	public Path createInstrumentPath() {
		if (instrumentPath == null) {
			instrumentPath = new Path(getProject());
		}
		return instrumentPath.createPath();
	}

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}
	
	void checkParameters() {
		if (instrumentPath == null) {
			throw new BuildException("Instrument path is not specified. Must specify at least one instrument path.");
		}
		instrumentPaths = new ArrayList<File>();
		for (String each : (String[]) instrumentPath.list()) {
			log("Instrument path: " + each);
			instrumentPaths.add(new File(each));
		}
		
		if (destDir == null) {
			throw new BuildException("Destination directory is not specified.");
		}

		if (metaDataFile == null) {
			metaDataFile = new File(destDir, "undercover.md");
		}
		
		if (coverageDataFile == null) {
			coverageDataFile = new File(destDir, "undercover.cd");
		}
		
		if (instrument == null) {
			instrument = new OfflineInstrument();
		}
	}
	
	public void execute() throws BuildException {
        log("Instrumenting...");
        checkParameters();
    	try {
    		instrument.setInstrumentPaths(instrumentPaths);
    		instrument.setOutputDirectory(destDir);
    		instrument.setMetaDataFile(metaDataFile);
			instrument.fullcopy();
		} catch (Exception e) {
			throw new BuildException(e);
		}
    }
}
