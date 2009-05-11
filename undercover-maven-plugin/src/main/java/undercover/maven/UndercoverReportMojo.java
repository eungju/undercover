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

import undercover.report.html.HtmlReport;

/**
 * Instruments, tests, and generates an Undercover report.
 *
 * @goal undercover
 * @execute phase="test" lifecycle="undercover"
 * @requiresDependencyResolution test
 */
public class UndercoverReportMojo extends AbstractMavenReport {
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
        	for (String each : Arrays.asList(project.getBuild().getSourceDirectory(), project.getBuild().getTestSourceDirectory())) {
            	File file = new File(each);
            	if (file.exists()) {
            		paths.add(file);
            	}
        	}
			getLog().info("Source paths: " + paths);
			sourcePaths = paths.toArray(new File[paths.size()]);
		}
    }
    
	protected void executeReport(Locale locale) throws MavenReportException {
		checkParameters();
		
		try {
			HtmlReport report = new HtmlReport();
			report.setSourcePaths(sourcePaths);
			report.setOutputDirectory(outputDirectory);
			report.generate();
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
