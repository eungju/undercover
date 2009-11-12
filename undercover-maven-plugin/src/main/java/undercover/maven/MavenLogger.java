package undercover.maven;

import org.apache.maven.plugin.logging.Log;

import undercover.support.Logger;

public class MavenLogger implements Logger {
	private final Log log;

	public MavenLogger(Log log) {
		this.log = log;
	}

	public void debug(String message) {
		log.debug(message);
	}

	public void info(String message) {
		log.info(message);
	}
	
	public void error(String message) {
		log.error(message);
	}
}
