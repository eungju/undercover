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

import undercover.metric.ClassMetric;
import undercover.metric.MetaData;

//FIXME: How to test instrumented bytecode?
public class InstrumentTest {
	private Instrument dut;
	private ClassMetric classMetric;
	
	public String traceBytecode(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		TraceClassVisitor trace = new TraceClassVisitor(writer);
		cr.accept(trace, 0);
		return buffer.toString();
	}
	
	@Before public void beforeEach() throws IOException {
		dut = new Instrument();
		byte[] original = IOUtils.toByteArray(getClass().getResourceAsStream("HelloWorld.class"));
		MetaData metaData = new MetaData();
		traceBytecode(dut.instrument(metaData, original));
		classMetric = metaData.getClass(HelloWorld.class.getName().replaceAll("\\.", "/"));
	}
	
	@Test public void abstractMethod() throws IOException {
		assertEquals(1, classMetric.getMethod("abstractMethod").blocks().size());
	}

	@Test public void emptyMethod() throws IOException {
		assertEquals(1, classMetric.getMethod("empty").blocks().size());
	}

	@Test public void ifBranchMethod() throws IOException {
		assertEquals(2, classMetric.getMethod("ifBranch").blocks().size());
	}

	@Test public void ifElseIfBranchesMethod() throws IOException {
		assertEquals(3, classMetric.getMethod("ifElseIfBranches").blocks().size());
	}
	
	@Test public void shortCircuitBranchMethod() throws IOException {
		assertEquals(2, classMetric.getMethod("shortCircuitBranch").blocks().size());
	}
}
