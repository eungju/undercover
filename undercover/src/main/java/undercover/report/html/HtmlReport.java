package undercover.report.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;
import undercover.support.FileUtils;
import undercover.support.IOUtils;
import undercover.support.xml.DoctypeDeclaration;
import undercover.support.xml.Element;
import undercover.support.xml.XmlDeclaration;
import undercover.support.xml.XmlWriter;

public class HtmlReport {
	private ReportData reportData;
	private File outputDirectory;
	private String encoding = "UTF-8";
	
	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public void setEncoding(String encoding) {
		this.encoding  = encoding;
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
				"jquery.flot.js",
				"excanvas.js",
				"undercover.js",
		};
		for (String each : resources) {
			copyResource("resources/" + each, each);
		}
	}

	void copyResource(String sourcePath, String destPath) throws IOException {
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
	
	void generateProjectReport() throws IOException {
		generateProjectPackages();
		generateProjectSummary();
		generateProjectClasses();
	}
	
	void generateProjectPackages() throws IOException {
		write(new MenuPage(reportData).build(), "project-packages.html");
	}

	void generateProjectSummary() throws IOException {
		write(new ProjectSummaryPage(reportData).build(), "project-summary.html");
	}

	void generateProjectClasses() throws IOException {
		write(new ProjectClassListPage(reportData).build(), "project-classes.html");
	}

	void generatePackageReports() throws IOException {
		for (PackageItem each : reportData.getPackages()) {
			generatePackageSummary(each);
			generatePackageClasses(each);
		}
	}

	void generatePackageSummary(PackageItem packageItem) throws IOException {
		write(new PackageSummaryPage(packageItem).build(), "package-" + packageItem.getLinkName() + "-summary.html");
	}

	void generatePackageClasses(PackageItem packageItem) throws IOException {
		write(new PackageClassListPage(packageItem).build(), "package-" + packageItem.getLinkName() + "-classes.html");
	}

	void generateSourceReports() throws IOException {
		for (SourceItem each : reportData.getSources()) {
			generateSourceSummary(each);
		}
	}
	
	void generateSourceSummary(SourceItem sourceItem) throws IOException {
		write(new SourceSummaryPage(sourceItem).build(), "source-" + sourceItem.getLinkName() + ".html");
	}
	
	void generateDashboardReport() throws IOException {
		write(new DashboardPage(reportData).build(), "project-dashboard.html");
	}

	void write(Element root, String path) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(openWriter(path));
			XmlWriter xmlWriter = new XmlWriter(writer);
			xmlWriter.visitXmlDeclaration(new XmlDeclaration("1.0", "UTF-8"));
			xmlWriter.visitDoctypeDeclaration(new DoctypeDeclaration("html", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "-//W3C//DTD XHTML 1.0 Transitional//EN"));
			root.accept(xmlWriter);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	Writer openWriter(String path) throws IOException {
		return new OutputStreamWriter(openOutputStream(path), encoding);
	}
	
	OutputStream openOutputStream(String path) throws IOException {
		return FileUtils.openOutputStream(getOutputFile(path));
	}

	File getOutputFile(String path) {
		return new File(outputDirectory, path);
	}
}
