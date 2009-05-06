package undercover.instrument;

import static org.junit.Assert.*;

import org.junit.Test;

public class InstrumentTest {
	@Test public void instrument() {
		byte[] bareClass = new byte[1];
		byte[] instrumentedClass = new byte[2];
		Instrument dut = new Instrument();
		assertArrayEquals(instrumentedClass, dut.instrument(bareClass));
	}
}
