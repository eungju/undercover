package undercover.report.html;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplateWriter;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class HtmlReport {
	private File[] sourcePaths;
	private File outputDir;
	String sourceEncoding = "UTF-8";
	final String outputEncoding = "UTF-8";
	final String templateEncoding = "UTF-8";
	StringTemplateGroup templateGroup;
	
	public HtmlReport() throws IOException {
		templateGroup = new StringTemplateGroup(new InputStreamReader(getClass().getResourceAsStream("default.stg"), templateEncoding), DefaultTemplateLexer.class);
		templateGroup.registerRenderer(String.class, new StringRenderer());
	}
	
	public void setSourcePaths(File[] sourcePaths) {
		this.sourcePaths = sourcePaths;
	}
	
	public void setOutputDirectory(File outputDirectory) {
		this.outputDir = outputDirectory;
	}
	
	public void generate() throws IOException {
		outputDir.mkdirs();
		
		copyResources();
		for (File each : findAllSourceFiles()) {
			new SourceReport(this, each).writeTo(outputDir);
		}
	}
	
	void copyResources() throws IOException {
		final String[] resources = {
				"SyntaxHighlighter/scripts/shCore.js",
				"SyntaxHighlighter/scripts/shBrushJava.js",
				"SyntaxHighlighter/scripts/shBrushScala.js",
				"SyntaxHighlighter/scripts/clipboard.swf",
				"SyntaxHighlighter/styles/help.png",
				"SyntaxHighlighter/styles/magnifier.png",
				"SyntaxHighlighter/styles/page_white_code.png",
				"SyntaxHighlighter/styles/page_white_copy.png",
				"SyntaxHighlighter/styles/printer.png",
				"SyntaxHighlighter/styles/shCore.css",
				"SyntaxHighlighter/styles/shThemeDefault.css",
				"SyntaxHighlighter/styles/shThemeDjango.css",
				"SyntaxHighlighter/styles/shThemeEmacs.css",
				"SyntaxHighlighter/styles/shThemeFadeToGrey.css",
				"SyntaxHighlighter/styles/shThemeMidnight.css",
				"SyntaxHighlighter/styles/shThemeRDark.css",
				"SyntaxHighlighter/styles/wrapping.png",
		};
		for (String each : resources) {
			copyResource("resources/" + each, each);
		}
	}

	private void copyResource(String sourcePath, String destPath) throws IOException {
		getOutputFile(destPath).getParentFile().mkdirs();
		InputStream input = null;
		OutputStream output = null;
		try {
			input = getClass().getResourceAsStream(sourcePath);
			output = openOutputStream(destPath);
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public StringTemplate getTemplate(String templateName) {
		return templateGroup.getInstanceOf(templateName);
	}

	public void writeTemplate(StringTemplate template, String relativeOutputPath) throws IOException {
		Writer out = new OutputStreamWriter(openOutputStream(relativeOutputPath), outputEncoding);
		StringTemplateWriter writer = new AutoIndentWriter(out);
		try {
			template.write(writer);
		} finally	{
			IOUtils.closeQuietly(out);
		}
	}
	
	public File getOutputFile(String path) {
		return new File(outputDir, path);
	}
	
	public OutputStream openOutputStream(String path) throws FileNotFoundException {
		return new FileOutputStream(getOutputFile(path));
	}

	public String getRelativeSourcePath(File file) {
		return getRelativeSourcePath(sourcePaths, file);
	}
	
	public String getRelativeSourcePath(File[] paths, File file) {
		for (File each : paths) {
			if (file.getAbsolutePath().startsWith(each.getAbsolutePath() + File.separator)) {
				return file.getAbsolutePath().substring(each.getAbsolutePath().length() + 1);
			}
		}
		throw new IllegalArgumentException("Cannot find " + file);
	}

	public List<File> findAllSourceFiles() {
		List<File> result = new ArrayList<File>();
		for (File each : sourcePaths) {
			result.addAll(FileUtils.listFiles(each, new RegexFileFilter(".*\\.(java|scala)"), TrueFileFilter.TRUE));
		}
		return result;
	}

	public List<File> findSourceFiles(String fileName) {
		List<File> result = new ArrayList<File>();
		for (File each : sourcePaths) {
			result.addAll(FileUtils.listFiles(each, new NameFileFilter(fileName), TrueFileFilter.TRUE));
		}
		return result;
	}
}
