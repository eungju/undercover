package undercover.report.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.FileUtils;
import undercover.support.HtmlUtils;
import undercover.support.xml.DoctypeDeclaration;
import undercover.support.xml.Element;
import undercover.support.xml.XmlDeclaration;
import undercover.support.xml.XmlWriter;

public class CoberturaXmlReport {
	private ReportData reportData;
	
	public CoberturaXmlReport(ReportData reportData) {
		this.reportData = reportData;
	}

	public void writeTo(File file, String encoding) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(FileUtils.openOutputStream(file), encoding));
			writeTo(writer, encoding);
		} finally {
			writer.close();
		}
	}

	public void writeTo(PrintWriter writer, String encoding) {
		XmlWriter xmlWriter = new XmlWriter(writer);
		xmlWriter.visitXmlDeclaration(new XmlDeclaration("1.0", encoding));
		xmlWriter.visitDoctypeDeclaration(new DoctypeDeclaration("coverage", "http://cobertura.sourceforge.net/xml/coverage-04.dtd"));
		Element root = buildCoverage(reportData);
		root.accept(xmlWriter);
	}

	Element buildCoverage(ReportData reportData) {
		ReportData item = reportData;
		Element result = new Element("coverage");
		result.attr("line-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("branch-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("lines-covered", item.getBlockMetrics().getCoverage().part);
		result.attr("lines-valid", item.getBlockMetrics().getCoverage().whole);
		result.attr("branches-covered", item.getBlockMetrics().getCoverage().part);
		result.attr("branches-valid", item.getBlockMetrics().getCoverage().whole);
		result.attr("complexity", item.getMethodMetrics().getComplexity().getAverage());
		result.attr("version", "1.9.2");
		result.attr("timestamp", System.currentTimeMillis());
		result.append(buildPackages(reportData.getPackages()));
		return result;
	}

	Element buildPackages(Collection<PackageItem> items) {
		Element result = new Element("packages");
		for (PackageItem each : items) {
			result.append(buildPackage(each));
		}
		return result;
	}

	Element buildPackage(PackageItem item) {
		Element result = new Element("package");
		result.attr("name", item.getDisplayName());
		result.attr("line-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("branch-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("complexity", item.getMethodMetrics().getComplexity().getAverage());
		result.append(buildClasses(item.classes));
		return result;
	}

	Element buildClasses(Collection<ClassItem> items) {
		Element result = new Element("classes");
		for (ClassItem each : items) {
			result.append(buildClass(each));
		}
		return result;
	}

	Element buildClass(ClassItem item) {
		Element result = new Element("class");
		result.attr("name", item.getDisplayName());
		result.attr("filename", item.source.getName());
		result.attr("line-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("branch-rate", item.getBlockMetrics().getCoverage().getRatio());
		result.attr("complexity", item.getMethodMetrics().getComplexity().getAverage());
		result.append(buildMethods(item.methods), buildLines());
		return result;
	}

	Element buildMethods(Collection<MethodItem> items) {
		Element result = new Element("methods");
		for (MethodItem each : items) {
			result.append(buildMethod(each));
		}
		return result;
	}
	
	Element buildMethod(MethodItem item) {
		int startOfDesc = item.getName().indexOf('(');
		String name = item.getName().substring(0, startOfDesc);
		String desc = item.getName().substring(startOfDesc);
		return new Element("method")
			.attr("name", HtmlUtils.escape(name))
			.attr("signature", desc)
			.attr("line-rate", item.getBlockMetrics().getCoverage().getRatio())
			.attr("branch-rate", item.getBlockMetrics().getCoverage().getRatio())
			.append(buildLines());
	}

	Element buildLines() {
		return new Element("lines");
	}
}
