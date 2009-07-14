package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import undercover.support.Proportion;

public class BlockMetricsTest {
	@Test public void executable() {
		BlockMetrics dut = new BlockMetrics(1, 4, 2, Arrays.asList(new MethodItem("m()V", 1, 1, 1)));
		assertEquals(1 + 1, dut.getComplexity());
		assertEquals(new Proportion(2 + 1, 4 + 1), dut.getCoverage());
		assertTrue(dut.isExecutable());
	}
	
	@Test public void notExecutable() {
		BlockMetrics dut = new BlockMetrics(1, 0, 0);
		assertFalse(dut.isExecutable());
		assertEquals(1.0, dut.getCoverage().getRatio(), 0);
	}

	@Test public void getRisk() {
		assertEquals(1, new BlockMetrics(1, 2, 2).getRisk(), 0);
		assertEquals(1 + 1, new BlockMetrics(1, 2, 0).getRisk(), 0);
	}
}
