package undercover.report.html;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.support.xml.Element;

public class CoverageComplexityGraphTest {
	private CoverageComplexityGraph dut;

	@Before public void beforeEach() {
		dut = new CoverageComplexityGraph(Collections.<ClassItem>emptySet());
	}
	
	@Test public void build() {
		Element actual = dut.build();
		assertEquals("coverage-complexity", actual.attr("id"));
	}
}
