package undercover.runtime;

import org.junit.Before;
import org.junit.Test;


public class ProbeTest {
	private Probe dut;
	private UndercoverSettings settings;

	@Before public void beforeEach() {
		settings = new UndercoverSettings();
		dut = new Probe(settings);
	}
	
	@Test public void register() {
		dut.register("p/c", new int[3][4]);
		dut.getCoverageData().getCoverage("p/c");
	}
}
