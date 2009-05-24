package undercover.instrument;

import undercover.runtime.Probe;

public class InstrumentedSample {
	public void empty() {
		__coverage__[999][1000]++;
	}

	public static int[][] __coverage__;
	static {
		preClinit();
	}
	
	private static void preClinit() {
		__coverage__ = new int[999][];
		
		__coverage__[999] = new int[9990000];
		
		Probe.INSTANCE.register("undercover/instrument/InstrumentedSample", __coverage__);
	}
	
}
