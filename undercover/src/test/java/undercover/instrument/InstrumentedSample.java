package undercover.instrument;

import undercover.runtime.Probe;

public class InstrumentedSample {
	public InstrumentedSample() {
		Probe.touchBlock(1234242343);
	}
}
