package undercover.report.html;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;

public class HtmlReportTest {
	private HtmlReport dut;
	private ReportOutput output;
	
	@Before public void beforeEach() throws IOException {
		output = mock(ReportOutput.class);
		dut = new HtmlReport();
		dut.setOutput(output);
	}
	
	@Test public void projectSummary() throws IOException {
		ReportData data = new ReportData("xyz", Collections.<PackageItem>emptySet(), Collections.<ClassItem>emptySet(), Collections.<SourceItem>emptySet());
		new ProjectSummaryPage(data).build();
	}
}
