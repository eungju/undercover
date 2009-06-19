package undercover.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
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
	private Project project;

	@Before public void beforeEach() {
		dut = new InstrumentTask();
		instrument = mockery.mock(OfflineInstrument.class);
		dut.instrument = instrument;
		project = new Project();
	}
	
	@Test(expected=BuildException.class)
	public void checkInstrumentPath() {
		dut.checkParameters();
	}

	@Test(expected=BuildException.class)
	public void checkDestDir() {
		dut.setInstrumentPath(new Path(project));
		dut.checkParameters();
	}
}
