package undercover.support;

import java.util.logging.Level;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JdkLoggerTest {
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	private JdkLogger dut;
	private java.util.logging.Logger logger;
	
	@Before public void beforeEach() {
		logger = mockery.mock(java.util.logging.Logger.class);
		dut = new JdkLogger(logger);
	}
	
	@Test public void debug() {
		mockery.checking(new Expectations() {{
			one(logger).log(Level.FINE, "message");
		}});
		dut.debug("message");
	}
	
	@Test public void info() {
		mockery.checking(new Expectations() {{
			one(logger).log(Level.INFO, "message");
		}});
		dut.info("message");
	}
	
	@Test public void error() {
		mockery.checking(new Expectations() {{
			one(logger).log(Level.SEVERE, "message");
		}});
		dut.error("message");
	}
}
