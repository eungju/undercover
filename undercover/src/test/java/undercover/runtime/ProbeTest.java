package undercover.runtime;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import undercover.UndercoverSettings;

public class ProbeTest {
	private Probe dut;
	private UndercoverSettings settings;

	@Before public void beforeEach() {
		settings = new UndercoverSettings();
		dut = new Probe(settings);
	}
	
	@Test public void touchBlock() {
		UUID blockId = UUID.randomUUID();
		dut.touchBlock(blockId.toString());
		dut.getCoverageData().getBlock(UUID.randomUUID());
	}
}
