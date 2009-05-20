package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.BlockCoverage;
import undercover.metric.BlockMeta;

public class LineCoverageAnalysisTest {
	private LineCoverageAnalysis dut;

	@Before public void beforeEach() {
		dut = new LineCoverageAnalysis();
	}
	
	@Test public void notCovered() {
		BlockMeta blockMeta = new BlockMeta(Arrays.asList(1));
		dut.analyze(blockMeta, new BlockCoverage(0));
		assertEquals(1, dut.getLineCount());
		assertEquals(0, dut.getCoveredLineCount());
	}

	@Test public void completelyCovered() {
		BlockMeta blockMeta = new BlockMeta(Arrays.asList(1));
		dut.analyze(blockMeta, new BlockCoverage(1));
		assertEquals(1, dut.getLineCount());
		assertEquals(1, dut.getCoveredLineCount());
	}

	@Test public void partialyCovered() {
		BlockMeta b1 = new BlockMeta(Arrays.asList(1));
		BlockMeta b2 = new BlockMeta(Arrays.asList(1));
		dut.analyze(b1, new BlockCoverage(1));
		dut.analyze(b2, new BlockCoverage(0));
		assertEquals(1, dut.getLineCount());
		assertEquals(0, dut.getCoveredLineCount());
		assertTrue(dut.getLine(1).isPartialyCovered());
	}
}
