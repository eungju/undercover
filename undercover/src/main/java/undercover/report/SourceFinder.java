package undercover.report;

import java.io.File;
import java.util.List;

import undercover.data.ClassMeta;
import undercover.support.JdkLogger;
import undercover.support.Logger;

public class SourceFinder {
	private final List<File> sourcePaths;
	private final String sourceEncoding;
	private Logger logger = new JdkLogger();

	public SourceFinder(List<File> sourcePaths) {
		this(sourcePaths, "UTF-8");
	}
	
	public SourceFinder(List<File> sourcePaths, String sourceEncoding) {
		this.sourcePaths = sourcePaths;
		this.sourceEncoding = sourceEncoding;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public SourceFile findSourceFile(ClassMeta classMeta) {
		String expectedPath = classMeta.getExpectedSourcePath();
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
