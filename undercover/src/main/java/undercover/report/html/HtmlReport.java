package undercover.report.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;
import undercover.support.IOUtils;
import undercover.support.xml.DoctypeDeclaration;
import undercover.support.xml.Element;
import undercover.support.xml.XmlDeclaration;
import undercover.support.xml.XmlWriter;

public class HtmlReport {
	private ReportData reportData;
	private ReportOutput output;
	
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
	
	void write(Element root, String path) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(output.openWriter(path));
			XmlWriter xmlWriter = new XmlWriter(writer);
			xmlWriter.visitXmlDeclaration(new XmlDeclaration("1.0", "UTF-8"));
			xmlWriter.visitDoctypeDeclaration(new DoctypeDeclaration("html", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "-//W3C//DTD XHTML 1.0 Transitional//EN"));
			root.accept(xmlWriter);
		} finally {
			IOUtils.closeQuietly(writer);
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
}
