package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SourceLineTest {
	private SourceLine dut;

	@Before public void beforeEach() {
		dut = new SourceLine(1, "Hello World");
	}
	
	@Test public void notExecutable() {
		assertFalse(dut.isExecutable());
	}
	
	@Test public void notCovered() {
		dut.addBlock(0);
		assertTrue(dut.isExecutable());
		assertFalse(dut.isCompletelyCovered());
		assertFalse(dut.isPartialyCovered());
	}

	@Test public void completelyCovered() {
		dut.addBlock(1);
		assertTrue(dut.isExecutable());
		assertTrue(dut.isCompletelyCovered());
		assertFalse(dut.isPartialyCovered());
	}

	@Test public void partialyCovered() {
		dut.addBlock(1);
		dut.addBlock(0);
		assertTrue(dut.isExecutable());
		assertFalse(dut.isCompletelyCovered());
		assertTrue(dut.isPartialyCovered());
	}
}
