package undercover.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

import undercover.data.CoverageData;
import undercover.data.MetaData;
import undercover.report.ReportData;
import undercover.report.ReportDataBuilder;
import undercover.report.ReportOutput;
import undercover.report.SourceFinder;
import undercover.report.html.HtmlReport;

public class ReportTask extends UndercoverTask {
	private File destDir;
	private Path sourcePath;
	private String sourceEncoding;
	
	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}
	
	public void addSourcePath(Path path) {
		sourcePath = path;
	}
	
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

    public void execute() throws BuildException {
    	log("Reporting...");
    	
    	List<File> sourcePaths = new ArrayList<File>();
    	for (String each : sourcePath.list()) {
    		sourcePaths.add(new File(each));
    	}
		SourceFinder sourceFinder = new SourceFinder(sourcePaths, sourceEncoding);
		try {
			CoverageData coverageData = coverageDataFile.exists() ? CoverageData.load(coverageDataFile) : new CoverageData();
			ReportDataBuilder builder = new ReportDataBuilder(coverageData);
			builder.setProjectName(getProject().getName());
			builder.setSourceFinder(sourceFinder);
			MetaData.load(metaDataFile).accept(builder);
			ReportData reportData = builder.getReportData();
			
			ReportOutput output = new ReportOutput(destDir);
			
			HtmlReport report = new HtmlReport();
			report.setReportData(reportData);
			report.setOutput(output);
			report.generate();
		} catch (IOException e) {
			throw new BuildException("Failed to generate report", e);
		}
    }
}
