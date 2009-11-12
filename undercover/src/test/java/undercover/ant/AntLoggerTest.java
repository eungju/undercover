package undercover.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AntLoggerTest {
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	private AntLogger dut;
	private Task task;
	
	@Before public void beforeEach() {
		task = mockery.mock(Task.class);
		dut = new AntLogger(task);
	}
	
	@Test public void debug() {
		mockery.checking(new Expectations() {{
			one(task).log("message", Project.MSG_DEBUG);
		}});
		dut.debug("message");
	}

	@Test public void info() {
		mockery.checking(new Expectations() {{
			one(task).log("message", Project.MSG_INFO);
		}});
		dut.info("message");
	}

	@Test public void error() {
		mockery.checking(new Expectations() {{
			one(task).log("message", Project.MSG_ERR);
		}});
		dut.error("message");
	}
}
