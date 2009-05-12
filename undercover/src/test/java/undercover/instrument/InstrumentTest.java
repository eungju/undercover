package undercover.instrument;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import undercover.metric.MetaData;

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
		//FIXME: How to test?
		traceBytecode(dut.instrument(new MetaData(), original));
		//assertEquals(traceBytecode(original), );
	}
}
