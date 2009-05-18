package undercover.report;

import java.io.File;
import java.util.List;

import undercover.metric.ClassMeta;

public class SourceFinder {
	private List<File> sourcePaths;

	public SourceFinder(List<File> sourcePaths) {
		this.sourcePaths = sourcePaths;
	}

	public File findSourceFile(ClassMeta classMeta) {
		for (File each : sourcePaths) {
			File file = new File(each, classMeta.getPackageName() + "/" + classMeta.source());
			if (file.exists() && file.isFile()) {
				return file;
			}
		}
		return null;
	}

	public String findSourcePath(ClassMeta classMeta) {
		File file = findSourceFile(classMeta);
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
