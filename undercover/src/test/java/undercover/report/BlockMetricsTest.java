package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class BlockMetricsTest {
	@Test public void executable() {
		BlockMetrics dut = new BlockMetrics(1, 4, 2, Arrays.asList(new MethodItem("m()V", 1, 1, 1)));
		assertEquals(1 + 1, dut.getComplexity());
		assertEquals(4 + 1, dut.getBlockCount());
		assertEquals(2 + 1, dut.getCoveredBlockCount());
		assertTrue(dut.isExecutable());
		assertEquals((double) dut.getCoveredBlockCount() / dut.getBlockCount(), dut.getCoverageRate(), 0.01);
	}
	
	@Test public void notExecutable() {
		BlockMetrics dut = new BlockMetrics(1, 0, 0);
		assertFalse(dut.isExecutable());
		assertEquals(1.0, dut.getCoverageRate(), 0.01);
	}

	@Test public void getRisk() {
		assertEquals(1, new BlockMetrics(1, 2, 2).getRisk(), 0.01);
		assertEquals(1 + 1, new BlockMetrics(1, 2, 0).getRisk(), 0.01);
	}
}
