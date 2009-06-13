package undercover.runtime;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class UndercoverSettingsTest {
	private UndercoverSettings dut;
	
	@Before public void beforeEach() {
		dut = new UndercoverSettings(new Properties());
	}
	
	@Test public void coverageSaveOnExit() {
		assertFalse(dut.isCoverageSaveOnExit());
		dut.setCoverageSaveOnExit(true);
		assertTrue(dut.isCoverageSaveOnExit());
	}
	
	@Test public void coverageFile() {
		assertNull(dut.getCoverageFile());
		File file = new File("/undercover.cd");
		dut.setCoverageFile(file);
		assertEquals(file.getAbsoluteFile(), dut.getCoverageFile().getAbsoluteFile());
	}
}
