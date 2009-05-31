package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.report.PackageItem;

public class ItemRendererTest {
	private ItemRenderer dut;

	@Before public void beforeEach() {
		dut = new ItemRenderer();
	}
	
	@Test public void linkNameFormat()  {
		assertEquals("p.p", dut.toString(new PackageItem("p/p"), "linkName"));
	}
}
