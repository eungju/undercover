package undercover.maven;

import java.io.File;
import java.util.Locale;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

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

	protected void executeReport(Locale locale) throws MavenReportException {
		// TODO Auto-generated method stub
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
