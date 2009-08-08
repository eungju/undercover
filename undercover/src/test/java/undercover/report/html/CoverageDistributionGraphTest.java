package undercover.report.html;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.support.xml.Element;

public class CoverageDistributionGraphTest {
	private CoverageDistributionGraph dut;

	@Before public void beforeEach() {
		dut = new CoverageDistributionGraph(Collections.<ClassItem>emptyList());
	}

	@Test public void zeroToNinePercent() {
		assertEquals(0, dut.coverageInterval(0));
		assertEquals(0, dut.coverageInterval(0.09));
	}

	@Test public void tenToNineteenPercent() {
		assertEquals(1, dut.coverageInterval(0.1));
		assertEquals(1, dut.coverageInterval(0.19));
	}

	@Test public void ninetyToHundredPercent() {
		assertEquals(9, dut.coverageInterval(0.9));
		assertEquals(9, dut.coverageInterval(1));
	}
	
	@Test public void build() {
		Element actual = dut.build();
		assertEquals("coverage-distribution", actual.attr("id"));
	}
}
