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
	EmmaXmlWriter writer;
	
	public EmmaXmlReport with(ReportData reportData) {
		this.reportData = reportData;
		return this;
	}
	
	public void writeTo(File file, String encoding) throws IOException {
		EmmaXmlWriter writer = null;
		try {
			file.getParentFile().mkdirs();
			writer = new EmmaXmlWriter(new PrintWriter(file, encoding), encoding);
			writeTo(writer);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public void writeTo(EmmaXmlWriter writer) {
		this.writer = writer;
		writeReport(reportData);		
	}

	void writeReport(ReportData reportData) {
		writer.document().report();
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
		writePackages(item.getPackages());
		writer.endAll().endData();
	}

	void writePackages(Collection<PackageItem> items) {
		for (PackageItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writePackage(each);
		}
	}
	
	void writePackage(PackageItem item) {
		writer.package_(item.getDisplayName())
			.coverage("class", item.getClassMetrics().getCoverage())
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		Set<SourceItem> sources = new HashSet<SourceItem>();
		for (ClassItem each : item.classes) {
			sources.add(each.getSource());
		}
		writeSources(sources);
		writer.endPackage();
	}
	
	void writeSources(Collection<SourceItem> items) {
		for (SourceItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeSource(each);
		}
	}

	void writeSource(SourceItem item) {
		writer.sourceFile(item.getSimpleName())
			.coverage("class", item.getClassMetrics().getCoverage())
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		writeClasses(item.classes);
		writer.endSourceFile();
	}
	
	void writeClasses(Collection<ClassItem> items) {
		for (ClassItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeClass(each);
		}
	}

	void writeClass(ClassItem item) {
		writer.class_(item.getSimpleName())
			.coverage("class", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1))
			.coverage("method", item.getMethodMetrics().getCoverage())
			.coverage("block", item.getBlockMetrics().getCoverage());
		writeMethods(item.methods);
		writer.endClass();
	}
	
	void writeMethods(Collection<MethodItem> items) {
		for (MethodItem each : items) {
			if (!each.getBlockMetrics().isExecutable()) {
				continue;
			}
			writeMethod(each);
		}
	}

	void writeMethod(MethodItem item) {
		writer.method(item.getDisplayName())
			.coverage("method", new Proportion(item.getBlockMetrics().isExecuted() ? 1 : 0, 1))
			.coverage("block", item.getBlockMetrics().getCoverage())
			.endMethod();
	}
}
