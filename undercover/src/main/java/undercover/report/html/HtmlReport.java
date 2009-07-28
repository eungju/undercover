package undercover.report.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import undercover.report.Item;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;
import undercover.support.IOUtils;
import undercover.support.xml.Comment;
import undercover.support.xml.Element;
import undercover.support.xml.Text;
import undercover.support.xml.XmlDeclaration;
import undercover.support.xml.XmlWriter;

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
		if (false) {
			StringTemplate template = getTemplate("projectPackages");
			template.setAttribute("project", reportData);
			output.write("project-packages.html", template.toString());
		} else {
			write(buildProjectPackagesPage(), "project-packages.html");
		}
	}
	
	void write(Element root, String path) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(output.openWriter(path));
			XmlWriter xmlWriter = new XmlWriter(writer);
			xmlWriter.visitXmlDeclaration(new XmlDeclaration("1.0", "UTF-8"));
			root.accept(xmlWriter);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	public static Element html() {
		return new Element("html");
	}
	
	public static Element body() {
		return new Element("body");
	}
	
	public static Element head() {
		return new Element("head");
	}

	public static Element title() {
		return new Element("title");
	}

	public static Element link() {
		return new Element("link");
	}

	public static Element script() {
		return new Element("script");
	}

	public static Element div() {
		return new Element("div");
	}

	public static Element ul() {
		return new Element("ul");
	}
	
	public static Element li() {
		return new Element("li");
	}

	public static Element a() {
		return new Element("a");
	}

	Element head(String title) {
		return head()
			.append(title().append(new Text(title)))
			.append(link().attr("rel", "stylesheet").attr("type", "text/css").attr("href", "style.css"))
			.append(script().attr("src", "jquery-1.3.2.min.js").attr("type", "text/javascript"))
			.append(new Comment("[if IE]><script src=\"excanvas.pack.js\" type=\"text/javascript\"></script><![endif]"))
			.append(script().attr("src", "jquery.flot.pack.js").attr("type", "text/javascript"))
			.append(script().attr("src", "undercover.js").attr("type", "text/javascript"));
	}

	Element roundedBox(Element innerBox) {
		return div().attr("class", "rounded-box")
			.append(div().attr("class", "round4"))
			.append(div().attr("class", "round2"))
			.append(div().attr("class", "round1"))
			.append(div().attr("class", "box-inner").append(innerBox))
			.append(div().attr("class", "round1"))
			.append(div().attr("class", "round2"))
			.append(div().attr("class", "round4"));
	}
	
	Text coveragePercent(Item item) {
		if (item.getBlockMetrics().isExecutable()) {
			return new Text(DoubleRenderer.trimZeros(String.format("%.1f", item.getBlockMetrics().getCoverage().getRatio() * 100)) + "%");
		} else {
			return new Text("N/A");
		}
	}
	
	Element buildProjectPackagesPage() {
		return html()	.append(
				head("Undercover"),
				body().append(
						roundedBox(new Element("h2").append(new Text("Undercover Coverage Report"))),
						div().attr("class", "navigation").append(
								buildNavigationMenu(),
								new Element("h3").append(new Text("Packages")),
								buildNavigationPackages())));
	}
	
	Element buildNavigationMenu() {
		return ul().attr("class", "menu")
			.append(li().append(a().attr("href", "project-dashboard.html").attr("target", "classPane").append(new Text("Dashboard"))))
			.append(li().append(a().attr("href", "project-summary.html").attr("target", "classPane").append(new Text("Coverage"))));
	}
	
	Element buildNavigationPackages() {
		Element result = ul().attr("class", "package-list");
		for (PackageItem each : reportData.getPackages()) {
			String summaryPage = "package-" + each.getLinkName() + "-summary.html";
			result.append(li()
					.append(a().attr("href", summaryPage).attr("target", "classPane").append(new Text(each.getDisplayName())))
					.append(new Text(" ("), coveragePercent(each), new Text(")")));
		}
		return result;
	}
	
	void generateProjectSummary() throws IOException {
		StringTemplate template = getTemplate("projectSummary");
		template.setAttribute("project", reportData);
		output.write("project-summary.html", template.toString());
	}

	void generateProjectClasses() throws IOException {
		StringTemplate template = getTemplate("projectClasses");
		template.setAttribute("classes", reportData.getClasses());
		output.write("project-classes.html", template.toString());
	}

	void generatePackageReports() throws IOException {
		for (PackageItem each : reportData.getPackages()) {
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
		for (SourceItem each : reportData.getSources()) {
			StringTemplate st = getTemplate("sourceSummary");
			st.setAttribute("source", each);
			output.write("source-" + each.getLinkName() + ".html", st.toString());
		}
	}
	
	void generateDashboardReport() throws IOException {
		StringTemplate template = getTemplate("dashboard");
		template.setAttribute("project", reportData);
		CoverageDistribution coverageDistribution = new CoverageDistribution(reportData.getClasses());
		template.setAttribute("coverageDistribution", coverageDistribution);
		CoverageComplexity coverageComplexity = new CoverageComplexity(reportData.getClasses());
		template.setAttribute("coverageComplexity", coverageComplexity);
		template.setAttribute("mostRiskyClasses", mostRisky(reportData.getClasses(), 20));
		template.setAttribute("mostComplexClasses", mostComplex(reportData.getClasses(), 10));
		template.setAttribute("leastCoveredClasses", leastCovered(reportData.getClasses(), 10));
		output.write("project-dashboard.html", template.toString());
	}

	<T extends Item> List<T> takeTopN(Collection<T> candidates, Comparator<T> comparator, int max) {
		List<T> items = new ArrayList<T>(candidates);
		Collections.sort(items, comparator);
		if (items.size() > max) {
			items = items.subList(0, max);
		}
		return items;
	}

	public <T extends Item> List<T> mostRisky(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return (int) Math.signum((b.getBlockMetrics().getRisk() - a.getBlockMetrics().getRisk()));
			}
		}, max);
	}

	public <T extends Item> List<T> mostComplex(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return b.getBlockMetrics().getComplexity() - a.getBlockMetrics().getComplexity();
			}
		}, max);
	}

	public <T extends Item> List<T> leastCovered(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return (int) Math.signum(a.getBlockMetrics().getCoverage().getRatio() - b.getBlockMetrics().getCoverage().getRatio());
			}
		}, max);
	}

	public StringTemplate getTemplate(String templateName) {
		return templateGroup.getInstanceOf(templateName);
	}
}
