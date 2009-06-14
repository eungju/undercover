package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PackageItemTest {
	private PackageItem dut;

	@Before public void beforeEach() {
		dut = new PackageItem("p");
	}
	
	@Test public void getDisplayName() {
		assertEquals("p", dut.getDisplayName());
	}
	
	@Test public void getLinkName() {
		assertEquals("p", dut.getLinkName());
	}

	@Test public void metrics() {
		assertNotNull(dut.getBlockMetrics());
		assertNotNull(dut.getMethodMetrics());
		assertNotNull(dut.getClassMetrics());
		assertNull(dut.getPackageMetrics());
	}
}
