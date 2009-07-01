package undercover.report.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.SortedSet;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ProjectItem;
import undercover.report.ReportData;
import undercover.support.HtmlUtils;

public class CoberturaXmlReport {
	private ReportData reportData;
	private File output;

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public void setOutput(File output) {
		this.output = output;
	}

	public void generate() throws IOException {
		StringBuilder builder = new StringBuilder();
		writeCoverage(builder, reportData);
		Writer writer = null;
		try {
			output.getParentFile().mkdirs();
			writer = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
			writer.write(builder.toString());
		} finally {
			writer.close();
		}
	}

	void writeCoverage(StringBuilder builder, ReportData reportData) {
		ProjectItem item = reportData.getProject();
		builder.append("<?xml version=\"1.0\"?>\n");
		builder.append("<!DOCTYPE coverage SYSTEM \"http://cobertura.sourceforge.net/xml/coverage-04.dtd\">\n");
		builder.append("<coverage")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" lines-covered=\"").append(item.getBlockMetrics().getCoveredBlockCount()).append("\"")
			.append(" lines-valid=\"").append(item.getBlockMetrics().getBlockCount()).append("\"")
			.append(" branches-covered=\"").append(item.getBlockMetrics().getCoveredBlockCount()).append("\"")
			.append(" branches-valid=\"").append(item.getBlockMetrics().getBlockCount()).append("\"")
			.append(" complexity=\"").append(item.getMethodMetrics().getAverageComplexity()).append("\"")
			.append(" version=\"").append("1.9.2").append("\"")
			.append(" timestamp=\"").append(System.currentTimeMillis()).append("\"")
			.append(">\n");
		writePackages(builder, reportData.getProject().packages);
		builder.append("</coverage>\n");
	}

	void writePackages(StringBuilder builder, SortedSet<PackageItem> items) {
		builder.append("<packages>\n");
		for (PackageItem each : items) {
			writePackage(builder, each);
		}
		builder.append("</packages>\n");
	}

	void writePackage(StringBuilder builder, PackageItem item) {
		builder.append("<package")
			.append(" name=\"").append(item.getDisplayName()).append("\"")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" complexity=\"").append(item.getMethodMetrics().getAverageComplexity()).append("\"")
			.append(">\n");
		writeClasses(builder, item.classes);
		builder.append("</package>\n");
	}

	void writeClasses(StringBuilder builder,	Collection<ClassItem> items) {
		builder.append("<classes>\n");
		for (ClassItem each : items) {
			writeClass(builder, each);
		}
		builder.append("</classes>\n");
	}

	void writeClass(StringBuilder builder, ClassItem item) {
		builder.append("<class")
			.append(" name=\"").append(item.getDisplayName()).append("\"")
			.append(" filename=\"").append(item.sourceFile.path).append("\"")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" complexity=\"").append(item.getMethodMetrics().getAverageComplexity()).append("\"")
			.append(">\n");
		writeMethods(builder, item.methods);
		builder.append("</class>\n");
	}

	void writeMethods(StringBuilder builder,	Collection<MethodItem> items) {
		builder.append("<methods>\n");
		for (MethodItem each : items) {
			writeMethod(builder, each);
		}
		builder.append("</methods>\n");
	}
	
	void writeMethod(StringBuilder builder, MethodItem item) {
		int startOfDesc = item.getName().indexOf('(');
		String name = item.getName().substring(0, startOfDesc);
		String desc = item.getName().substring(startOfDesc);
		builder.append("<method")
			.append(" name=\"").append(HtmlUtils.escape(name)).append("\"")
			.append(" signature=\"").append(desc).append("\"")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverageRate()).append("\"")
			.append(">\n");
		writeLines(builder);
		builder.append("</method>\n");
	}

	void writeLines(StringBuilder builder) {
		builder.append("<lines></lines>\n");
	}
}