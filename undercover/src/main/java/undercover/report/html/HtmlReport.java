package undercover.report.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.io.IOUtils;

import undercover.report.ClassItem;
import undercover.report.Item;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ProjectItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;

public class HtmlReport {
	final String templateEncoding = "UTF-8";

	private ReportData reportData;
	private ReportOutput output;
	String sourceEncoding = "UTF-8";
	
	private StringTemplateGroup templateGroup;
	
	public HtmlReport() throws IOException {
		templateGroup = new StringTemplateGroup(new InputStreamReader(getClass().getResourceAsStream("default.stg"), templateEncoding), DefaultTemplateLexer.class);
		templateGroup.registerRenderer(String.class, new StringRenderer());
		templateGroup.registerRenderer(Double.class, new DoubleRenderer());
		templateGroup.registerRenderer(ProjectItem.class, new ItemRenderer());
		templateGroup.registerRenderer(PackageItem.class, new ItemRenderer());
		templateGroup.registerRenderer(SourceItem.class, new ItemRenderer());
		templateGroup.registerRenderer(ClassItem.class, new ItemRenderer());
		templateGroup.registerRenderer(MethodItem.class, new ItemRenderer());
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.output = new ReportOutput(outputDirectory);
	}
	
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	
	public void generate() throws IOException {
		copyResources();

		generateProjectPackages();
		generateProjectSummary();
		generateProjectClasses();
		
		generatePackageReports();

		generateSourceReports();
	}
	
	void copyResources() throws IOException {
		final String[] resources = {
				"style.css",
				"jquery-1.3.2.min.js",
				"index.html",
		};
		for (String each : resources) {
			copyResource("resources/" + each, each);
		}
	}

	void copyResource(String sourcePath, String destPath) throws IOException {
		InputStream input = null;
		try {
			input = getClass().getResourceAsStream(sourcePath);
			output.write(destPath, input);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	void generateProjectPackages() throws IOException {
		StringTemplate template = getTemplate("projectPackages");
		template.setAttribute("project", reportData.getProject());
		output.write("project-packages.html", template);
	}
	
	void generateProjectSummary() throws IOException {
		StringTemplate template = getTemplate("projectSummary");
		template.setAttribute("project", reportData.getProject());
		output.write("project-summary.html", template);
	}

	void generateProjectClasses() throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", reportData.getAllClasses());
		output.write("project-classes.html", template);
	}

	void generatePackageReports() throws IOException {
		for (PackageItem each : reportData.getProject().packages) {
			generatePackageSummary(each);
			generatePackageClasses(each);
		}
	}

	private void generatePackageSummary(PackageItem packageItem) throws IOException {
		StringTemplate template = getTemplate("packageSummary");
		template.setAttribute("package", packageItem);
		output.write("package-" + packageItem.getLinkName() + "-summary.html", template);
	}

	void generatePackageClasses(PackageItem packageItem) throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", packageItem.classes);
		output.write("package-" + packageItem.getDisplayName() + "-classes.html", template);
	}

	void generateSourceReports() throws IOException {
		for (SourceItem each : reportData.getAllSources()) {
			StringTemplate st = getTemplate("sourceSummary");
			st.setAttribute("source", each);
			output.write("source-" + each.getLinkName() + ".html", st);
		}
	}

	public StringTemplate getTemplate(String templateName) {
		return templateGroup.getInstanceOf(templateName);
	}
}
