package undercover.report;

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodItemTest {
	@Test public void properties() {
		MethodItem dut = new MethodItem("m()V", 1, 0, 0);
		assertEquals("m()V", dut.getName());
		assertEquals("m() : void", dut.getDisplayName());
		assertNotNull(dut.getBlockMetrics());
		assertNull(dut.getMethodMetrics());
		assertNull(dut.getClassMetrics());
		assertNull(dut.getPackageMetrics());
	}

	@Test public void oneParameter() {
		MethodItem dut = new MethodItem("m(I)V", 1, 0, 0);
		assertEquals("m(int) : void", dut.getDisplayName());
	}

	@Test public void multipleParameters() {
		MethodItem dut = new MethodItem("m(IZ)V", 1, 0, 0);
		assertEquals("m(int, boolean) : void", dut.getDisplayName());
	}
}
