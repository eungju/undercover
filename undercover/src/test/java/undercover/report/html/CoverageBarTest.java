package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Test;

import undercover.report.MethodItem;
import undercover.support.xml.Element;

public class CoverageBarTest {
	@Test public void available() {
		CoverageBar dut = new CoverageBar(new MethodItem("m()V", 1, 2, 1));
		Element actual = dut.build();
		assertEquals("coverage-bar", actual.attr("class"));
		assertEquals("negative", ((Element) actual.children.get(0)).attr("class"));
	}

	@Test public void notAvailable() {
		CoverageBar dut = new CoverageBar(new MethodItem("m()V", 1, 0, 0));
		Element actual = dut.build();
		assertEquals("not-available", ((Element) actual.children.get(0)).attr("class"));
	}
}
