package undercover.ant;

import org.apache.tools.ant.Task;

import undercover.instrument.OfflineInstrument;

public class UndercoverTask extends Task {
    public void execute() {
        // use of the reference to Project-instance
        String message = getProject().getProperty("ant.project.name");

        // Task's log method
        log("Here is project '" + message + "'.");

        // where this task is used?
        log("I am used in: " +  getLocation() );
    }
    
    public String createInstrument() {
    	new OfflineInstrument();
    	return "instrument";
    }
    
    public String createReport() {
    	return "report";
    }
}
