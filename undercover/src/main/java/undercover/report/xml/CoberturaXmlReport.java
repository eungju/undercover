package undercover.report.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.HtmlUtils;

public class CoberturaXmlReport {
	private ReportData reportData;
	private File output;
	private String encoding = "UTF-8";
	
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
			writer = new OutputStreamWriter(new FileOutputStream(output), encoding);
			writer.write(builder.toString());
		} finally {
			writer.close();
		}
	}

	void writeCoverage(StringBuilder builder, ReportData reportData) {
		ReportData item = reportData;
		builder.append("<?xml version=\"1.0\"")
				.append(" encoding=\"").append(encoding).append("\"")
				.append("?>\n");
		builder.append("<!DOCTYPE coverage SYSTEM \"http://cobertura.sourceforge.net/xml/coverage-04.dtd\">\n");
		builder.append("<coverage")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(" lines-covered=\"").append(item.getBlockMetrics().getCoverage().part).append("\"")
			.append(" lines-valid=\"").append(item.getBlockMetrics().getCoverage().whole).append("\"")
			.append(" branches-covered=\"").append(item.getBlockMetrics().getCoverage().part).append("\"")
			.append(" branches-valid=\"").append(item.getBlockMetrics().getCoverage().whole).append("\"")
			.append(" complexity=\"").append(item.getMethodMetrics().getAverageComplexity()).append("\"")
			.append(" version=\"").append("1.9.2").append("\"")
			.append(" timestamp=\"").append(System.currentTimeMillis()).append("\"")
			.append(">\n");
		writePackages(builder, reportData.getPackages());
		builder.append("</coverage>\n");
	}

	void writePackages(StringBuilder builder, Collection<PackageItem> items) {
		builder.append("<packages>\n");
		for (PackageItem each : items) {
			writePackage(builder, each);
		}
		builder.append("</packages>\n");
	}

	void writePackage(StringBuilder builder, PackageItem item) {
		builder.append("<package")
			.append(" name=\"").append(item.getDisplayName()).append("\"")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
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
			.append(" filename=\"").append(item.source.getName()).append("\"")
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
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
			.append(" line-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(" branch-rate=\"").append(item.getBlockMetrics().getCoverage().getRatio()).append("\"")
			.append(">\n");
		writeLines(builder);
		builder.append("</method>\n");
	}

	void writeLines(StringBuilder builder) {
		builder.append("<lines></lines>\n");
	}
}
