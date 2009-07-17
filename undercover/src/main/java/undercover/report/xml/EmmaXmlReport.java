package undercover.report.xml;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;
import undercover.support.Proportion;

public class EmmaXmlReport {
	private ReportData reportData;
	private File output;
	private String encoding = "UTF-8";
	private EmmaXmlWriter writer;
	
	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public void setOutput(File output) {
		this.output = output;
	}

	public void generate() throws IOException {
		writer = null;
		try {
			output.getParentFile().mkdirs();
			writer = new EmmaXmlWriter(new PrintWriter(output, encoding));
			writeReport(reportData);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	void writeReport(ReportData reportData) {
		writer.document(encoding).report();
		writeStats(reportData);
		writeData(reportData);
		writer.endReport().endDocument();
	}

	void writeStats(ReportData reportData) {
		writer.stats()
			.statsPackages(reportData.getPackageMetrics().getCount())
			.statsClasses(reportData.getClassMetrics().getCount())
			.statsMethods(reportData.getMethodMetrics().getCount())
			.statsSourceFiles(reportData.getSources().size())
			.statsSourceLines(countLines(reportData.getSources()))
			.endStats();
	}

	int countLines(Collection<SourceItem> sources) {
		int result = 0;
		for (SourceItem each : sources) {
			result += each.getLines().size();
		}
		return result;
	}

	void writeData(ReportData item) {
		writer.data().all()
			.coverage("class", item.getClassMetrics().getCoverage())
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		for (PackageItem each : item.getPackages()) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writePackage(each);
		}
		writer.endAll().endData();
	}
	
	void writePackage(PackageItem item) {
		writer.package_(item.getDisplayName())
			.coverage("class", item.getClassMetrics().getCoverage())
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		Set<SourceItem> sources = new HashSet<SourceItem>();
		for (ClassItem each : item.classes) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			sources.add(each.getSource());
		}
		for (SourceItem each : sources) {
			writeSource(each);
		}
		writer.endPackage();
	}

	void writeSource(SourceItem item) {
		writer.sourceFile(item.getSimpleName())
			.coverage("class", item.getClassMetrics().getCoverage())
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		for (ClassItem each : item.classes) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeClass(each);
		}
		writer.endSourceFile();
	}

	void writeClass(ClassItem item) {
		writer.class_(item.getSimpleName())
			.coverage("class", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1))
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		for (MethodItem each : item.methods) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeMethod(each);
		}
		writer.endClass();
	}

	void writeMethod(MethodItem item) {
		writer.method(item.getDisplayName())
			.coverage("method", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1))
			.coverage("block", item.getBlockMetrics().getCoverage())
			.endMethod();
	}
}
