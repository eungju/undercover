package undercover.ant;

import java.io.File;

import org.apache.tools.ant.Task;

public abstract class UndercoverTask extends Task {
	protected File metaDataFile;
	protected File coverageDataFile;

	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}

	protected void checkMetaDataFile() {
		if (metaDataFile == null) {
			metaDataFile = new File("undercover.md");
		}
	}

	public void setCoverageDataFile(File coverageDataFile) {
		this.coverageDataFile = coverageDataFile;
	}

	protected void checkCoverageDataFile() {
		if (coverageDataFile == null) {
			coverageDataFile = new File("undercover.cd");
		}
	}
}
