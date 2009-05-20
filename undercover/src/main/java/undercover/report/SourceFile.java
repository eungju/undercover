package undercover.report;

import java.io.File;

/**
 * A file on a source path.
 */
public class SourceFile {
	public final File root;
	public final String path;
	public final File file;
	
	public SourceFile(File root, File file) {
		this.root = root;
		if (file.getAbsolutePath().startsWith(root.getAbsolutePath() + File.separator)) {
			path = file.getAbsolutePath().substring(root.getAbsolutePath().length() + 1).replaceAll("\\\\", "/");
		} else {
			throw new IllegalArgumentException();
		}
		this.file = file;
	}

	public SourceFile(File root, String path) {
		this.root = root;
		this.path = path;
		this.file = new File(root, path);
	}
	
	public SourceFile(String path) {
		this.root = null;
		this.path = path;
		this.file = null;
	}

	public boolean isExist() {
		return root != null;
	}
}
