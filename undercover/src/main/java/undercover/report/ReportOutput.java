package undercover.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateWriter;
import org.apache.commons.io.IOUtils;

public class ReportOutput {
	private final File directory;
	private final String encoding;
	
	public ReportOutput(File directory) {
		this.directory = directory;
		this.encoding = "UTF-8";
		directory.mkdirs();
	}
	
	public void write(String path, InputStream input) throws IOException {
		getOutputFile(path).getParentFile().mkdirs();
		OutputStream output = null;
		try {
			output = openOutputStream(path);
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	public void write(String path, StringTemplate template) throws IOException {
		Writer out = new OutputStreamWriter(openOutputStream(path), encoding);
		StringTemplateWriter writer = new AutoIndentWriter(out);
		try {
			template.write(writer);
		} finally	{
			IOUtils.closeQuietly(out);
		}
	}

	OutputStream openOutputStream(String path) throws FileNotFoundException {
		return new FileOutputStream(getOutputFile(path));
	}

	File getOutputFile(String path) {
		return new File(directory, path);
	}
}