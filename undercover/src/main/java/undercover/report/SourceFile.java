package undercover.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * A file on a source path.
 */
public class SourceFile {
	public final File root;
	public final String path;
	public final File file;
	public String encoding;

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
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isExist() {
		return root != null;
	}
	
	public Reader openReader() throws IOException {
		return new InputStreamReader(new FileInputStream(file), encoding);
	}
}
