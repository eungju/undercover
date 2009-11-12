package undercover.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import undercover.support.Logger;

public class AntLogger implements Logger {	
	private final Task task;

	public AntLogger(Task task) {
		this.task = task;
	}

	public void debug(String message) {
		task.log(message, Project.MSG_DEBUG);
	}

	public void error(String message) {
		task.log(message, Project.MSG_ERR);
	}

	public void info(String message) {
		task.log(message, Project.MSG_INFO);
	}
}
