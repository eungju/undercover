package undercover.ant;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import undercover.report.html.HtmlReport;

public class ReportTask extends Task {
    public void execute() throws BuildException {
    	try {
			new HtmlReport();
	        log("I'm reporter.");
		} catch (IOException e) {
			throw new BuildException(e);
		}
    }
}
