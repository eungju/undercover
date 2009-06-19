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
	private Path sourcePath;
	private String sourceEncoding;
	private List<ReportFormat> formats = new ArrayList<ReportFormat>();
	
	public void addSourcePath(Path path) {
		sourcePath = path;
	}
	
	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	
	public ReportFormat createHtml() {
		ReportFormat format = new HtmlFormat();
		formats.add(format);
		return format;
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
			
			for (ReportFormat each : formats) {
				each.setReportData(reportData);
				each.generate();
			}
		} catch (IOException e) {
			throw new BuildException("Failed to generate report", e);
		}
    }
    
    public static abstract class ReportFormat {
		protected ReportData reportData;
    	protected File dest;
    	
    	public void setReportData(ReportData reportData) {
    		this.reportData = reportData;
    	}
    	
    	public void setDest(File dest) {
    		this.dest = dest;
    	}
    		
    	public abstract void generate() throws IOException ;
    }
    
    public static class HtmlFormat extends ReportFormat {
		public void generate() throws IOException {
			ReportOutput output = new ReportOutput(dest);
			HtmlReport report = new HtmlReport();
			report.setReportData(reportData);
			report.setOutput(output);
			report.generate();
		}
    }
}
