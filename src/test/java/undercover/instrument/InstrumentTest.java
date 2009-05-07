package undercover.instrument;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

public class InstrumentTest {
	private Instrument dut;

	public String traceBytecode(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		TraceClassVisitor trace = new TraceClassVisitor(writer);
		cr.accept(trace, 0);
		return buffer.toString();
	}
	
	@Before public void beforeEach() {
		dut = new Instrument();
	}
	
	@Test public void instrument() throws IOException {
		byte[] original = IOUtils.toByteArray(getClass().getResourceAsStream("HelloWorld.class"));
		assertEquals(traceBytecode(original), traceBytecode(dut.instrument(original)));
	}
}
