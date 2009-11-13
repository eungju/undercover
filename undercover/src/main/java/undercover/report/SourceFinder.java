package undercover.report;

import java.io.File;
import java.util.Collections;
import java.util.List;

import undercover.data.ClassMeta;
import undercover.support.JdkLogger;
import undercover.support.Logger;

/**
 * Find source file of the class.
 */
public class SourceFinder {
	private Logger logger = new JdkLogger();
	private List<File> sourcePaths = Collections.emptyList();
	private String sourceEncoding = "UTF-8";

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setSourcePaths(List<File> sourcePaths) {
		this.sourcePaths = sourcePaths;
		logger.info("Source paths: " + this.sourcePaths);
	}
	
	public void setSourceEncoding(String soureEncoding) {
		this.sourceEncoding = soureEncoding;
		logger.info("Source encoding: " + this.sourceEncoding);
	}

	public SourceFile findSourceFile(ClassMeta classMeta) {
		String expectedPath = getExpectedSourcePath(classMeta);
		SourceFile result = findSourceFile(expectedPath);
		if (result == null) {
			result = new SourceFile(expectedPath);
			logger.debug("Did not found the source file for class " + classMeta.name);
		} else {
			logger.debug("Found the source file for class " + classMeta.name + " at " + result.file);
		}
		result.setEncoding(sourceEncoding);
		return result;
	}

	public String getExpectedSourcePath(ClassMeta classMeta) {
		return classMeta.getPackageName() + "/" + classMeta.source;
	}

	SourceFile findSourceFile(String expectedPath) {
		for (File each : sourcePaths) {
			File file = new File(each, expectedPath);
			if (file.exists() && file.isFile()) {
				return new SourceFile(each, file);
			}
		}
		return null;
	}
}
