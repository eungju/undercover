package undercover.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;

import undercover.instrument.OfflineInstrument;
import undercover.instrument.filter.GlobFilter;

public class InstrumentTask extends UndercoverTask {
	Path instrumentPath;
	File destDir;
	PatternSet filterPatternSet;
	
	OfflineInstrument instrument;
	List<File> instrumentPaths;
	GlobFilter filter;
	
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
	
	public PatternSet createFilter() {
		filterPatternSet = new PatternSet();
		return filterPatternSet;
	}
	
	void checkParameters() {
		checkInstrumentPath();
		checkDestDir();
		checkMetaDataFile();
		checkCoverageDataFile();
		checkFilter();
		
		if (instrument == null) {
			instrument = new OfflineInstrument();
		}
	}

	void checkInstrumentPath() {
		if (instrumentPath == null) {
			throw new BuildException("Instrument path is not specified. Must specify at least one instrument path.");
		}

		instrumentPaths = new ArrayList<File>();
		for (String each : (String[]) instrumentPath.list()) {
			instrumentPaths.add(new File(each));
		}
		log("Instrument path: " + instrumentPaths);
	}

	void checkDestDir() {
		if (destDir == null) {
			throw new BuildException("Destination directory is not specified.");
		}
	}
	
	void checkFilter() {
		String[] includes = filterPatternSet.getIncludePatterns(getProject());
		if (includes == null) {
			includes = new String[0];
		}
		String[] excludes = filterPatternSet.getExcludePatterns(getProject());
		if (excludes == null) {
			excludes = new String[0];
		}
		filter = new GlobFilter(includes, excludes);
	}
	
	public void execute() throws BuildException {
        log("Instrumenting...");
        checkParameters();
    	try {
    		instrument.setInstrumentPaths(instrumentPaths);
    		instrument.setOutputDirectory(destDir);
    		instrument.setMetaDataFile(metaDataFile);
    		instrument.setFilter(filter);
			instrument.fullcopy();
		} catch (Exception e) {
			throw new BuildException(e);
		}
    }
}
