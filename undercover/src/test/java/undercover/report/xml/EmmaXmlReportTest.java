package undercover.report.xml;

import static org.junit.Assert.*;

import org.junit.Test;

import undercover.support.Proportion;

public class EmmaXmlReportTest {
	@Test public void coverage() {
		EmmaXmlReport dut = new EmmaXmlReport();
		StringBuilder builder = new StringBuilder();
		dut.writeCoverage(builder, "block", new Proportion(1, 2));
		assertEquals("<coverage type=\"block, %\" value=\"50% (1/2)\" />\n", builder.toString());
	}
}
