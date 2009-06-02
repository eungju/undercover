package undercover.report;

import java.io.File;
import java.util.List;

import undercover.metric.ClassMeta;

public class SourceFinder {
	private final List<File> sourcePaths;
	private final String sourceEncoding;

	public SourceFinder(List<File> sourcePaths) {
		this(sourcePaths, "UTF-8");
	}
	
	public SourceFinder(List<File> sourcePaths, String sourceEncoding) {
		this.sourcePaths = sourcePaths;
		this.sourceEncoding = sourceEncoding;
	}

	public SourceFile findSourceFile(ClassMeta classMeta) {
		String expectedPath = getExpectedSourcePath(classMeta);
		SourceFile result = new SourceFile(expectedPath);
		for (File each : sourcePaths) {
			File file = new File(each, expectedPath);
			if (file.exists() && file.isFile()) {
				result = new SourceFile(each, file);
				break;
			}
		}
		result.setEncoding(sourceEncoding);
		return result;
	}

	public String getExpectedSourcePath(ClassMeta classMeta) {
		return classMeta.getPackageName() + "/" + classMeta.source;
	}
}
