package undercover.ant;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tools.ant.BuildException;
import org.junit.Before;
import org.junit.Test;

import undercover.instrument.OfflineInstrument;

public class InstrumentTaskTest {
	private InstrumentTask dut;
	private OfflineInstrument instrument;

	@Before public void beforeEach() {
		dut = new InstrumentTask();
		instrument = mock(OfflineInstrument.class);
		dut.instrument = instrument;
	}
	
	@Test(expected=BuildException.class)
	public void instrumentPathIsRequired() {
		dut.checkInstrumentPath();
	}
	
	@Test public void instrumentPath() throws IOException {
		File dir = File.createTempFile("classes", "");
		dut.createInstrumentPath().createPathElement().setLocation(dir);
		dut.checkInstrumentPath();
		assertEquals(Arrays.asList(dir), dut.instrumentPaths);
	}

	@Test public void destDir() throws IOException {
		File dir = File.createTempFile("undercover", "");
		dut.setDestDir(dir);
		dut.checkDestDir();
		assertEquals(dir, dut.destDir);
	}
	
	@Test(expected=BuildException.class)
	public void destDirIsRequired() {
		dut.checkDestDir();
	}
}
