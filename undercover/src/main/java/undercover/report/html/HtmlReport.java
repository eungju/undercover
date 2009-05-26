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
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;

public class HtmlReport {
	final String templateEncoding = "UTF-8";
	final String outputEncoding = "UTF-8";

	private ReportData reportData;
	private File outputDirectory;
	private File[] sourcePaths;
	String sourceEncoding = "UTF-8";
	
	private StringTemplateGroup templateGroup;
	
	public HtmlReport() throws IOException {
		templateGroup = new StringTemplateGroup(new InputStreamReader(getClass().getResourceAsStream("default.stg"), templateEncoding), DefaultTemplateLexer.class);
		templateGroup.registerRenderer(String.class, new StringRenderer());
		templateGroup.registerRenderer(Double.class, new DoubleRenderer());
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setSourcePaths(File[] sourcePaths) {
		this.sourcePaths = sourcePaths;
	}
	
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	
	public void generate() throws IOException {
		outputDirectory.mkdirs();
		copyResources();

		generateProjectPackages();
		generateProjectSummary();
		generateProjectClasses();
		
		generatePackageReports();
		
		for (SourceItem each : reportData.getAllSources()) {
			new SourceReport(this, each).writeTo(outputDirectory);
		}
	}
	
	void copyResources() throws IOException {
		final String[] resources = {
				"style.css",
				"index.html",
		};
		for (String each : resources) {
			copyResource("resources/" + each, each);
		}
	}

	void copyResource(String sourcePath, String destPath) throws IOException {
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

	void generateProjectPackages() throws IOException {
		StringTemplate template = getTemplate("projectPackages");
		template.setAttribute("project", reportData.getProject());
		writeTemplate(template, "project-packages.html");
	}
	
	void generateProjectSummary() throws IOException {
		StringTemplate template = getTemplate("projectSummary");
		template.setAttribute("project", reportData.getProject());
		writeTemplate(template, "project-summary.html");
	}

	void generateProjectClasses() throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", reportData.getAllClasses());
		writeTemplate(template, "project-classes.html");
	}

	void generatePackageReports() throws IOException {
		for (PackageItem each : reportData.getProject().packages) {
			generatePackageReport(each);
		}
	}

	private void generatePackageReport(PackageItem packageItem) throws IOException {
		StringTemplate template = getTemplate("packageSummary");
		template.setAttribute("package", packageItem);
		writeTemplate(template, packageItem.getLink());
	}

	public StringTemplate getTemplate(String templateName) {
		return templateGroup.getInstanceOf(templateName);
	}

	public void writeTemplate(StringTemplate template, String path) throws IOException {
		Writer out = new OutputStreamWriter(openOutputStream(path), outputEncoding);
		StringTemplateWriter writer = new AutoIndentWriter(out);
		try {
			template.write(writer);
		} finally	{
			IOUtils.closeQuietly(out);
		}
	}
	
	public File getOutputFile(String path) {
		return new File(outputDirectory, path);
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
}
