package undercover.ant;

import org.apache.tools.ant.Task;

import undercover.instrument.OfflineInstrument;

public class InstrumentTask extends Task {
    public void execute() {
    	new OfflineInstrument();
        log("I'm instrumenter.");
    }
}
