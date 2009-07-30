package undercover.report.html;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
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
	
	@Test public void lowCoverageIsRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 1, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(Arrays.asList(m1, m2), 10));
	}

	@Test public void highComplexityRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 1);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(Arrays.asList(m1, m2), 10));
	}
	
	@Test public void mostComplex() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 2);
		assertEquals(Arrays.asList(m2, m1), dut.mostComplex(Arrays.asList(m1, m2), 10));
	}

	@Test public void leastCovered() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.leastCovered(Arrays.asList(m1, m2), 10));
	}
}
