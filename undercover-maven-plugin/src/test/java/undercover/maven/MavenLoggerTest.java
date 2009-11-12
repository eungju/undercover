package undercover.maven;

import org.apache.maven.plugin.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class MavenLoggerTest {
	private Mockery mockery = new JUnit4Mockery();
	private MavenLogger dut;
	private Log log;

	@Before public void beforeEach() {
		log = mockery.mock(Log.class);
		dut = new MavenLogger(log);
	}
	
	@Test public void debug() {
		mockery.checking(new Expectations() {{
			one(log).debug("message");
		}});
		dut.debug("message");
	}
	
	@Test public void info() {
		mockery.checking(new Expectations() {{
			one(log).info("message");
		}});
		dut.info("message");
	}
	
	@Test public void error() {
		mockery.checking(new Expectations() {{
			one(log).error("message");
		}});
		dut.error("message");
	}
}
