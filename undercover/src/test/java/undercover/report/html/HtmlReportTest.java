package undercover.report.html;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.ProjectItem;
import undercover.report.ReportData;
import undercover.report.ReportOutput;
import undercover.report.SourceItem;
import undercover.support.UndercoverMockery;

@RunWith(JMock.class)
public class HtmlReportTest {
	private HtmlReport dut;
	private Mockery mockery = new UndercoverMockery();
	private ReportOutput output;
	
	@Before public void beforeEach() throws IOException {
		output = mockery.mock(ReportOutput.class);
		dut = new HtmlReport();
		dut.setOutput(output);
	}
	
	@Test public void projectSummary() throws IOException {
		mockery.checking(new Expectations() {{
			one(output).write(with(equal("project-summary.html")), with(any(String.class)));
		}});
	
		ReportData data = new ReportData(new ProjectItem("xyz"), new HashMap<String, ClassItem>(), new HashMap<String, SourceItem>());
		dut.setReportData(data);
		dut.generateProjectSummary();
	}
	
	@Test public void lowCoverageIsRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 1, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(10, Arrays.asList(m1, m2)));
	}

	@Test public void highComplexityRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 1);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(10, Arrays.asList(m1, m2)));
	}
	
	@Test public void mostComplex() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostComplex(10, Arrays.asList(m1, m2)));
	}
}
