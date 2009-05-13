package undercover.report.html;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringRendererTest {
	private StringRenderer dut;

	@Before public void beforeEach() {
		dut = new StringRenderer();
	}
	
	@Test public void defaultFormat() {
		assertEquals("<>", dut.toString("<>"));
	}
	
	@Test public void htmlFormat() {
		assertEquals("&lt;&gt;", dut.toString("<>", "html"));
	}
}
