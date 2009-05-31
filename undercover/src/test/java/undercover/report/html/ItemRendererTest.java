package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.report.MethodItem;
import undercover.report.PackageItem;

public class ItemRendererTest {
	private ItemRenderer dut;

	@Before public void beforeEach() {
		dut = new ItemRenderer();
	}
	
	@Test public void defaultRendering()  {
		assertEquals("p.p", dut.toString(new PackageItem("p/p")));
	}

	@Test public void linkNameFormat()  {
		assertEquals("p.p", dut.toString(new PackageItem("p/p"), "linkName"));
	}
	
	@Test public void coveragePercent() {
		assertEquals("N/A", dut.toString(new MethodItem("m()V", 1, 0, 0), "coveragePercent"));
		assertEquals("100.0%", dut.toString(new MethodItem("m()V", 1, 1, 1), "coveragePercent"));
	}
}
