package undercover.support;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdkLogger implements undercover.support.Logger {
	private final Logger logger;

	public JdkLogger() {
		this(Logger.getLogger("undercover"));
	}
	
	public JdkLogger(Logger logger) {
		this.logger = logger;
	}

	public void debug(String message) {
		logger.log(Level.FINE, message);
	}

	public void info(String message) {
		logger.log(Level.INFO, message);
	}

	public void error(String message) {
		logger.log(Level.SEVERE, message);
	}
}
