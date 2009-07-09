package undercover.report.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import undercover.report.BlockMetrics;
import undercover.report.ClassItem;
import undercover.report.Item;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;
import undercover.support.HtmlUtils;

public class EmmaXmlReport {
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
		writeReport(builder, reportData);
		Writer writer = null;
		try {
			output.getParentFile().mkdirs();
			writer = new OutputStreamWriter(new FileOutputStream(output), encoding);
			writer.write(builder.toString());
		} finally {
			writer.close();
		}
	}

	void writeReport(StringBuilder builder, ReportData reportData) {
		builder.append("<?xml version=\"1.0\"")
				.append(" encoding=\"").append(encoding).append("\"")
				.append("?>\n");
		builder.append("<report>\n");
		writeStats(builder, reportData);
		writeData(builder, reportData);
		builder.append("</report>\n");
	}

	void writeStats(StringBuilder builder, ReportData reportData) {
		builder.append("<stats>\n");
		builder.append("<packages")
			.append(" value=\"").append(reportData.getPackageMetrics().getCount()).append("\"")
			.append(" />\n");
		builder.append("<classes")
			.append(" value=\"").append(reportData.getClassMetrics().getCount()).append("\"")
			.append(" />\n");
		builder.append("<methods")
			.append(" value=\"").append(reportData.getMethodMetrics().getCount()).append("\"")
			.append(" />\n");
		builder.append("<srcfiles")
			.append(" value=\"").append(reportData.getSources().size()).append("\"")
			.append(" />\n");
		builder.append("<srclines")
			.append(" value=\"").append(countLines(reportData.getSources())).append("\"")
			.append(" />\n");
		builder.append("</stats>\n");
	}

	int countLines(Collection<SourceItem> sources) {
		int result = 0;
		for (SourceItem each : sources) {
			result += each.getLines().size();
		}
		return result;
	}

	void writeData(StringBuilder builder, ReportData reportData) {
		builder.append("<data>\n");
		builder.append("<all name=\"all classes\">\n");
		writeCoverage(builder, reportData);
		writePackages(builder, reportData.getPackages());
		builder.append("</all>\n");
		builder.append("</data>\n");
	}
	
	void writeCoverage(StringBuilder builder, Item item) {
		BlockMetrics blockMetrics = item.getBlockMetrics();
		builder.append("<coverage")
			.append(" type=\"block,%\"")
			.append(" value=\"")
				.append(String.format("%.0f", blockMetrics.getCoverageRate() * 100)).append("%")
				.append(" (")
				.append(blockMetrics.getCoveredBlockCount())
				.append("/")
				.append(blockMetrics.getBlockCount())
				.append(")\"")
			.append(" />\n");
	}

	void writePackages(StringBuilder builder, Collection<PackageItem> items) {
		for (PackageItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writePackage(builder, each);
		}
	}

	void writePackage(StringBuilder builder, PackageItem item) {
		builder.append("<package")
			.append(" name=\"").append(item.getDisplayName()).append("\"")
			.append(">\n");
		writeCoverage(builder, item);
		Set<SourceItem> sources = new HashSet<SourceItem>();
		for (ClassItem each : item.classes) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			sources.add(each.getSource());
		}
		writeSources(builder, sources);
		builder.append("</package>\n");
	}

	void writeSources(StringBuilder builder, Collection<SourceItem> items) {
		for (SourceItem each : items) {
			writeSource(builder, each);
		}
	}

	void writeSource(StringBuilder builder, SourceItem item) {
		builder.append("<srcfile")
			.append(" name=\"").append(item.getSimpleName()).append("\"")
			.append(">\n");
		writeCoverage(builder, item);
		writeClasses(builder, item.classes);
		builder.append("</srcfile>\n");
	}

	void writeClasses(StringBuilder builder, Collection<ClassItem> items) {
		for (ClassItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeClass(builder, each);
		}
	}

	void writeClass(StringBuilder builder, ClassItem item) {
		builder.append("<class")
			.append(" name=\"").append(item.getSimpleName()).append("\"")
			.append(">\n");
		writeCoverage(builder, item);
		writeMethods(builder, item.methods);
		builder.append("</class>\n");
	}

	void writeMethods(StringBuilder builder, Collection<MethodItem> items) {
		for (MethodItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeMethod(builder, each);
		}
	}
	
	void writeMethod(StringBuilder builder, MethodItem item) {
		builder.append("<method")
			.append(" name=\"").append(HtmlUtils.escape(item.getDisplayName())).append("\"")
			.append(">\n");
		writeCoverage(builder, item);
		builder.append("</method>\n");
	}
}
