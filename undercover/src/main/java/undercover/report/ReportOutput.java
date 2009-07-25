package undercover.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import undercover.support.FileUtils;
import undercover.support.IOUtils;

public class ReportOutput {
	private final File directory;
	private final String encoding;
	
	public ReportOutput(File directory) {
		this(directory, "UTF-8");
	}
	
	public ReportOutput(File directory, String encoding) {
		this.directory = directory;
		this.encoding = encoding;
		directory.mkdirs();
	}
	
	public void write(String path, InputStream input) throws IOException {
		OutputStream output = null;
		try {
			output = openOutputStream(path);
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	public void write(String path, String input) throws IOException {
		Writer out = new OutputStreamWriter(openOutputStream(path), encoding);
		try {
			out.write(input);
		} finally	{
			IOUtils.closeQuietly(out);
		}
	}

	OutputStream openOutputStream(String path) throws IOException {
		return FileUtils.openOutputStream(getOutputFile(path));
	}

	File getOutputFile(String path) {
		return new File(directory, path);
	}
}
