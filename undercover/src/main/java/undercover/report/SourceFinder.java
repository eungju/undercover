package undercover.report;

import java.io.File;
import java.util.List;

import undercover.metric.ClassMeta;

public class SourceFinder {
	private List<File> sourcePaths;

	public SourceFinder(List<File> sourcePaths) {
		this.sourcePaths = sourcePaths;
	}

	public SourceFile findSourceFile(ClassMeta classMeta) {
		String expectedPath = getExpectedSourcePath(classMeta);
		for (File each : sourcePaths) {
			File file = new File(each, expectedPath);
			if (file.exists() && file.isFile()) {
				return new SourceFile(each, file);
			}
		}
		return new SourceFile(expectedPath);
	}

	public String getExpectedSourcePath(ClassMeta classMeta) {
		return classMeta.getPackageName() + "/" + classMeta.source();
	}
}
