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
		assertEquals("0.1", dut.toString(0.1D));
	}
	
	@Test public void percentFormat() {
		assertEquals("100.0%", dut.toString(1D, "percent"));
	}
}
