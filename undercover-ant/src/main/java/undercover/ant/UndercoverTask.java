package undercover.ant;

import java.io.File;

import org.apache.tools.ant.Task;

public abstract class UndercoverTask extends Task {
	protected File metaDataFile;
	protected File coverageDataFile;

	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}
	
	public void setCoverageDataFile(File coverageDataFile) {
		this.coverageDataFile = coverageDataFile;
	}
}
