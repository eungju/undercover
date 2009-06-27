package undercover.instrument.filter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class GlobFilterTest {
	@Test public void includeAll() {
		GlobFilter dut = new GlobFilter(Collections.<String>emptyList(), Collections.<String>emptyList());
		assertTrue(dut.accept("p/c"));
	}

	@Test public void excludeSomething() {
		GlobFilter dut = new GlobFilter(Collections.<String>emptyList(), Arrays.asList("p/c"));
		assertFalse(dut.accept("p/c"));
		assertTrue(dut.accept("p/d"));
		assertTrue(dut.accept("p2/c"));
	}

	@Test public void includeExceptSomething() {
		GlobFilter dut = new GlobFilter(Arrays.asList("p/**"), Arrays.asList("p/c"));
		assertFalse(dut.accept("p1/c"));
		assertFalse(dut.accept("p/c"));
		assertTrue(dut.accept("p/d"));
	}
}
