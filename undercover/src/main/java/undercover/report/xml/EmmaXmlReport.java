package undercover.report.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;
import undercover.support.FileUtils;
import undercover.support.Proportion;
import undercover.support.xml.Element;
import undercover.support.xml.XmlDeclaration;
import undercover.support.xml.XmlWriter;

public class EmmaXmlReport {
	private ReportData reportData;
	
	public EmmaXmlReport(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public void writeTo(File file, String encoding) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(FileUtils.openOutputStream(file), encoding));
			writeTo(writer, encoding);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void writeTo(PrintWriter writer, String encoding) {
		XmlWriter xmlWriter = new XmlWriter(writer);
		xmlWriter.visitXmlDeclaration(new XmlDeclaration("1.0", encoding));
		buildReport(reportData).accept(xmlWriter);		
	}

	Element buildReport(ReportData reportData) {
		return new Element("report")
			.append(buildStats(reportData), buildData(reportData));
	}

	Element buildStats(ReportData reportData) {
		return new Element("stats")
			.append(new Element("packages").attr("value", reportData.getPackageMetrics().getCount()))
			.append(new Element("classes").attr("value", reportData.getClassMetrics().getCount()))
			.append(new Element("methods").attr("value", reportData.getMethodMetrics().getCount()))
			.append(new Element("srcfiles").attr("value", reportData.getSources().size()))
			.append(new Element("srclines").attr("value", countLines(reportData.getSources())));
	}

	int countLines(Collection<SourceItem> sources) {
		int result = 0;
		for (SourceItem each : sources) {
			result += each.getLines().size();
		}
		return result;
	}

	Element buildData(ReportData item) {
		return new Element("data").append(new Element("all")
			.append(buildCoverage("class", item.getClassMetrics().getCoverage()))
			.append(buildCoverage("method", item.getMethodMetrics().getCoverage()))
			.append(buildCoverage("block", item.getBlockMetrics().getCoverage()))
			.append(buildPackages(item.getPackages())));
	}

	Element buildCoverage(String type, Proportion coverage) {
		return new Element("coverage")
			.attr("type", type + ", %")
			.attr("value", String.format("%.0f%% (%d/%d)", coverage.getRatio() * 100, coverage.part, coverage.whole));
	}

	List<Element> buildPackages(Collection<PackageItem> items) {
		List<Element> result = new ArrayList<Element>();
		for (PackageItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			result.add(buildPackage(each));
		}
		return result;
	}
	
	Element buildPackage(PackageItem item) {
		Set<SourceItem> sources = new HashSet<SourceItem>();
		for (ClassItem each : item.classes) {
			sources.add(each.getSource());
		}
		return new Element("package")
			.attr("name", item.getDisplayName())
			.append(buildCoverage("class", item.getClassMetrics().getCoverage()))
			.append(buildCoverage("method", item.getMethodMetrics().getCoverage()))
			.append(buildCoverage("block", item.getBlockMetrics().getCoverage()))
			.append(buildSources(sources));
	}
	
	List<Element> buildSources(Collection<SourceItem> items) {
		List<Element> result = new ArrayList<Element>();
		for (SourceItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			result.add(buildSource(each));
		}
		return result;
	}

	Element buildSource(SourceItem item) {
		return new Element("srcfile")
			.attr("name", item.getSimpleName())
			.append(buildCoverage("class", item.getClassMetrics().getCoverage()))
			.append(buildCoverage("method", item.getMethodMetrics().getCoverage()))
			.append(buildCoverage("block", item.getBlockMetrics().getCoverage()))
			.append(buildClasses(item.classes));
	}
	
	List<Element> buildClasses(Collection<ClassItem> items) {
		List<Element> result = new ArrayList<Element>();
		for (ClassItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			result.add(buildClass(each));
		}
		return result;
	}

	Element buildClass(ClassItem item) {
		return new Element("class").attr("name", item.getSimpleName())
			.append(buildCoverage("class", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1)))
			.append(buildCoverage("method", item.getMethodMetrics().getCoverage()))
			.append(buildCoverage("block", item.getBlockMetrics().getCoverage()))
			.append(buildMethods(item.methods));
	}
	
	List<Element> buildMethods(Collection<MethodItem> items) {
		List<Element> result = new ArrayList<Element>();
		for (MethodItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			result.add(buildMethod(each));
		}
		return result;
	}

	Element buildMethod(MethodItem item) {
		return new Element("method")
			.attr("name", item.getDisplayName())
			.append(buildCoverage("method", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1)))
			.append(buildCoverage("block", item.getBlockMetrics().getCoverage()));
	}
}
