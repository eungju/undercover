package undercover.report;

import java.io.File;
import java.util.List;

import undercover.metric.ClassMetric;

public class SourceFinder {
	private List<File> sourcePaths;

	public SourceFinder(List<File> sourcePaths) {
		this.sourcePaths = sourcePaths;
	}

	public File findSourceFile(ClassMetric classMetric) {
		for (File each : sourcePaths) {
			File file = new File(each, classMetric.getPackageName() + "/" + classMetric.source());
			if (file.exists() && file.isFile()) {
				return file;
			}
		}
		return null;
	}

	public String findSourcePath(ClassMetric classMetric) {
		File file = findSourceFile(classMetric);
		return file == null ? null : relativePath(file).replaceAll("/|\\\\", "/");
	}

	String relativePath(File file) {
		for (File each : sourcePaths) {
			if (file.getAbsolutePath().startsWith(each.getAbsolutePath() + File.separator)) {
				return file.getAbsolutePath().substring(each.getAbsolutePath().length() + 1);
			}
		}
		throw new IllegalArgumentException("Cannot find " + file);
	}
}
