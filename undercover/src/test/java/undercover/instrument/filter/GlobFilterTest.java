package undercover.instrument.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class GlobFilterTest {
	@Test public void includeAll() {
		GlobFilter dut = new GlobFilter(new String[0], new String[0]);
		assertTrue(dut.accept("p/c"));
	}

	@Test public void excludeSomething() {
		GlobFilter dut = new GlobFilter(new String[0], new String[] { "p/c" });
		assertFalse(dut.accept("p/c"));
		assertTrue(dut.accept("p/d"));
		assertTrue(dut.accept("p2/c"));
	}

	@Test public void includeExceptSomething() {
		GlobFilter dut = new GlobFilter(new String[] { "p/**" }, new String[] { "p/c" });
		assertFalse(dut.accept("p1/c"));
		assertFalse(dut.accept("p/c"));
		assertTrue(dut.accept("p/d"));
	}
}
