package undercover.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import undercover.data.CoverageData;
import undercover.data.MetaData;
import undercover.report.ReportData;
import undercover.report.ReportDataBuilder;
import undercover.report.ReportOutput;
import undercover.report.SourceFinder;
import undercover.report.html.HtmlReport;
import undercover.report.xml.CoberturaXmlReport;

/**
 * Instruments, tests, and generates an Undercover report.
 *
 * @goal undercover
 * @execute phase="test" lifecycle="undercover"
 * @requiresDependencyResolution test
 */
public class UndercoverReportMojo extends AbstractMavenReport {
    /**
     * Location to store class coverage metadata.
     *
     * @parameter expression="${undercover.metaDataFile}" default-value="${project.build.directory}/undercover/undercover.md"
     */
    protected File metaDataFile;

    /**
     * Location to store class coverage data.
     *
     * @parameter expression="${undercover.coverageDataFile}" default-value="${project.build.directory}/undercover/undercover.cd"
     */
    protected File coverageDataFile;

    /**
     * Output directory for the report.
     *
     * @parameter default-value="${project.reporting.outputDirectory}/undercover"
     * @required
     */
    protected File outputDirectory;

	/**
     * Source locations.
     *
     * @parameter
     */
    protected File[] sourcePaths;

    /**
     * Source encoding.
     *
     * @parameter expression="${undercover.report.source.encoding}" default-value="UTF-8"
     */
    protected String sourceEncoding;
    
    /**
     * Report formats.
     *
     * @parameter
     */
    protected String[] formats;
    
    /**
     * Output directory for the cobertura compatible report.
     *
     * @parameter default-value="${project.reporting.outputDirectory}/cobertura"
     * @required
     */
	protected File coberturaOutputDirectory;
	
    /**
     * Site renderer.
     *
     * @component
     */
    protected Renderer siteRenderer;

    /**
     * Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Check parameters.
     *
     * @throws MavenReportException if any parameters are wrong
     */
    protected void checkParameters() throws MavenReportException {
        if (sourcePaths == null) {
        	List<File> paths = new ArrayList<File>();
        	for (String each : Arrays.asList(project.getBuild().getSourceDirectory())) {
            	File file = new File(each);
            	if (file.exists()) {
            		paths.add(file);
            	}
        	}
			getLog().info("Source paths: " + paths);
			sourcePaths = paths.toArray(new File[paths.size()]);
		}
        
        if (formats == null) {
        	formats = new String[] { "html" };
        }
    }
    
	protected void executeReport(Locale locale) throws MavenReportException {
		checkParameters();
		
		SourceFinder sourceFinder = new SourceFinder(Arrays.asList(sourcePaths), sourceEncoding);
		try {
			CoverageData coverageData = coverageDataFile.exists() ? CoverageData.load(coverageDataFile) : new CoverageData();
			ReportDataBuilder builder = new ReportDataBuilder(coverageData);
			builder.setProjectName(project.getName());
			builder.setSourceFinder(sourceFinder);
			MetaData.load(metaDataFile).accept(builder);
			ReportData reportData = builder.getReportData();
			
			for (String format : formats) {
				getLog().info("Generating " + format + " report");
				if ("html".equals(format)) {
					HtmlReport report = new HtmlReport();
					report.setReportData(reportData);
					report.setOutput(new ReportOutput(outputDirectory, "UTF-8"));
					report.generate();
				} else if ("coberturaxml".equals(format)) {
					CoberturaXmlReport report = new CoberturaXmlReport();
					report.setReportData(reportData);
					report.setOutput(new File(coberturaOutputDirectory, "coverage.xml"));
					report.generate();
				} else {
					getLog().warn("Unknown report format " + format);
				}
			}
		} catch (IOException e) {
			throw new MavenReportException("Failed to generate report", e);
		}
	}

	protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
	}

	protected MavenProject getProject() {
        return project;
	}

	protected Renderer getSiteRenderer() {
        return siteRenderer;
	}

	public String getDescription(Locale locale) {
        return "Undercover Test Coverage Report";
	}

	public String getName(Locale locale) {
        return "Undercover Test Coverage";
	}

	public String getOutputName() {
        return "undercover/index";
	}

	public boolean isExternalReport()
    {
        return true;
    }
}
