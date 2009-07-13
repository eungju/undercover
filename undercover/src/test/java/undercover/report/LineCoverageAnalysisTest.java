package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import undercover.data.BlockMeta;

public class LineCoverageAnalysisTest {
	private LineCoverageAnalysis dut;

	@Before public void beforeEach() {
		dut = new LineCoverageAnalysis();
	}
	
	@Test public void notCovered() {
		BlockMeta blockMeta = new BlockMeta(Arrays.asList(1));
		dut.analyze(blockMeta, 0);
		assertFalse(dut.getLine(1).isCompletelyCovered());
		assertFalse(dut.getLine(1).isPartialyCovered());
	}

	@Test public void completelyCovered() {
		BlockMeta blockMeta = new BlockMeta(Arrays.asList(1));
		dut.analyze(blockMeta, 1);
		assertTrue(dut.getLine(1).isCompletelyCovered());
		assertFalse(dut.getLine(1).isPartialyCovered());
	}

	@Test public void partialyCovered() {
		BlockMeta b1 = new BlockMeta(Arrays.asList(1));
		BlockMeta b2 = new BlockMeta(Arrays.asList(1));
		dut.analyze(b1, 1);
		dut.analyze(b2, 0);
		assertFalse(dut.getLine(1).isCompletelyCovered());
		assertTrue(dut.getLine(1).isPartialyCovered());
	}
}
