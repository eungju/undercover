package undercover.ant;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.tools.ant.BuildException;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import undercover.instrument.OfflineInstrument;

@RunWith(JMock.class)
public class InstrumentTaskTest {
	private InstrumentTask dut;
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	private OfflineInstrument instrument;

	@Before public void beforeEach() {
		dut = new InstrumentTask();
		instrument = mockery.mock(OfflineInstrument.class);
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
