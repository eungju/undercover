package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DoubleRendererTest {
	private DoubleRenderer dut;

	@Before public void beforeEach() {
		dut = new DoubleRenderer();
	}

	@Test public void defaultFormat() {
		assertEquals("0.12", dut.toString(0.123D));
		assertEquals("2", dut.toString(2D));
		assertEquals("0.8", dut.toString(0.8D));
	}
	
	@Test public void percentFormat() {
		assertEquals("99.9", dut.toString(0.999D, "percent"));
		assertEquals("100", dut.toString(1D, "percent"));
	}
}
