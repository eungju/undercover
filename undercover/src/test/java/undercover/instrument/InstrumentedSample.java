package undercover.instrument;

import java.util.UUID;

import undercover.runtime.Probe;

public class InstrumentedSample {
	public InstrumentedSample() {
		Probe.INSTANCE.touchBlock(UUID.randomUUID().toString());
	}
}
