package undercover.testbed;

import org.junit.Before;
import org.junit.Test;

public class SampleTest {
	private Sample dut;

	@Before public void beforeEach() {
		dut = new Sample();
	}
	
	@Test public void simple() {
		dut.simple();
	}
	
	@Test public void ifBranch() {
		dut.ifBranch(true);
	}

	@Test public void partialyCoveredIfBranch() {
		dut.ifBranch(true, false);
	}
}
