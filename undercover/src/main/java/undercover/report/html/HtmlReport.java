package undercover.report.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.apache.commons.io.IOUtils;

import undercover.report.MethodMeasure;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;

public class HtmlReport {
	final String templateEncoding = "UTF-8";

	private ReportData reportData;
	private ReportOutput output;
	
	private StringTemplateGroup templateGroup;
	
	public HtmlReport() throws IOException {
		templateGroup = new StringTemplateGroup(new InputStreamReader(getClass().getResourceAsStream("default.stg"), templateEncoding), DefaultTemplateLexer.class);
		templateGroup.registerRenderer(String.class, new StringRenderer());
		templateGroup.registerRenderer(Double.class, new DoubleRenderer());
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	public void setOutput(ReportOutput output) {
		this.output = output;
	}
	
	public void generate() throws IOException {
		copyResources();
		generateProjectReport();
		generatePackageReports();
		generateSourceReports();
		generateDashboardReport();
	}

	void copyResources() throws IOException {
		final String[] resources = {
				"index.html",
				"style.css",
				"jquery-1.3.2.min.js",
				"jquery.flot.pack.js",
				"excanvas.pack.js",
				"undercover.js",
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

	void generateProjectReport() throws IOException {
		generateProjectPackages();
		generateProjectSummary();
		generateProjectClasses();
	}
	
	void generateProjectPackages() throws IOException {
		StringTemplate template = getTemplate("projectPackages");
		template.setAttribute("project", reportData.getProject());
		output.write("project-packages.html", template.toString());
	}
	
	void generateProjectSummary() throws IOException {
		StringTemplate template = getTemplate("projectSummary");
		template.setAttribute("project", reportData.getProject());
		output.write("project-summary.html", template.toString());
	}

	void generateProjectClasses() throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", reportData.getAllClasses());
		output.write("project-classes.html", template.toString());
	}

	void generatePackageReports() throws IOException {
		for (PackageItem each : reportData.getProject().packages) {
			generatePackageSummary(each);
			generatePackageClasses(each);
		}
	}

	void generatePackageSummary(PackageItem packageItem) throws IOException {
		StringTemplate template = getTemplate("packageSummary");
		template.setAttribute("package", packageItem);
		output.write("package-" + packageItem.getLinkName() + "-summary.html", template.toString());
	}

	void generatePackageClasses(PackageItem packageItem) throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", packageItem.classes);
		output.write("package-" + packageItem.getLinkName() + "-classes.html", template.toString());
	}

	void generateSourceReports() throws IOException {
		for (SourceItem each : reportData.getAllSources()) {
			StringTemplate st = getTemplate("sourceSummary");
			st.setAttribute("source", each);
			output.write("source-" + each.getLinkName() + ".html", st.toString());
		}
	}
	
	void generateDashboardReport() throws IOException {
		StringTemplate template = getTemplate("dashboard");
		template.setAttribute("project", reportData.getProject());
		CoverageDistribution coverageDistribution = new CoverageDistribution(reportData.getAllClasses());
		template.setAttribute("coverageDistribution", coverageDistribution);
		CoverageComplexity coverageComplexity = new CoverageComplexity(reportData.getAllClasses());
		template.setAttribute("coverageComplexity", coverageComplexity);
		template.setAttribute("mostRiskyClasses", mostRisky(20, reportData.getAllClasses()));
		template.setAttribute("mostComplexPackages", mostComplex(10, reportData.getProject().packages));
		template.setAttribute("mostComplexClasses", mostComplex(10, reportData.getAllClasses()));
		output.write("project-dashboard.html", template.toString());
	}

	public <T extends MethodMeasure> List<T> mostRisky(int max, Collection<T> candidates) {
		List<T> items = new ArrayList<T>(candidates);
		Collections.sort(items, new Comparator<T>() {
			public int compare(T a, T b) {
				return (int) Math.signum((b.getRisk() - a.getRisk()));
			}
		});
		if (items.size() > max) {
			items = items.subList(0, max);
		}
		return items;
	}

	public <T extends MethodMeasure> List<T> mostComplex(int max, Collection<T> candidates) {
		List<T> items = new ArrayList<T>(candidates);
		Collections.sort(items, new Comparator<T>() {
			public int compare(T a, T b) {
				return b.getComplexity() - a.getComplexity();
			}
		});
		if (items.size() > max) {
			items = items.subList(0, max);
		}
		return items;
	}

	public StringTemplate getTemplate(String templateName) {
		return templateGroup.getInstanceOf(templateName);
	}
}
