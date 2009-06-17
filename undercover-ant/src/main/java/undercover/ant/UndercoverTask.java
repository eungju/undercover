package undercover.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class UndercoverTask extends Task {
	private List<Task> tasks = new ArrayList<Task>();
	
    public void execute() throws BuildException {
        // use of the reference to Project-instance
        String message = getProject().getProperty("ant.project.name");

        // Task's log method
        log("Here is project '" + message + "'.");

        // where this task is used?
        log("I am used in: " +  getLocation() );
        
        for (Task each : tasks) {
        	each.execute();
        }
    }
    
    public Task createInstrument() {
    	return addTask(new InstrumentTask(), "instrument");
    }
    
    public Task createReport() {
    	return addTask(new ReportTask(), "report");
    }

	Task addTask(Task task, String name) {
		//task.setTaskName(getTaskName() + "-" + name);
		task.setProject(getProject());
		task.setLocation(getLocation());
		task.setOwningTarget(getOwningTarget());
		task.init();
    	tasks.add(task);
    	return task;
	}
}
