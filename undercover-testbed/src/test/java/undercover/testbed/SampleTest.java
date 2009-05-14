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
}
