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
import undercover.metric.MethodMetric;

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
		MetaData metaData = dut.getMetaData();
		traceBytecode(dut.instrument(original));
		classMetric = metaData.getClass(HelloWorld.class.getName().replaceAll("\\.", "/"));
	}
	
	@Test public void abstractMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("abstractMethod()V");
		assertEquals(0, methodMetric.blocks().size());
		assertEquals(0, methodMetric.getConditionalBranches());
	}

	@Test public void block() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("b1()Z"); 
		assertEquals(1, methodMetric.blocks().size());
		assertEquals(0, methodMetric.getConditionalBranches());
	}

	@Test public void simple() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("simple()V"); 
		assertEquals(1, methodMetric.blocks().size());
		assertEquals(0, methodMetric.getConditionalBranches());
	}

	@Test public void sequential() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("sequential()V"); 
		assertEquals(1, methodMetric.blocks().size());
		assertEquals(0, methodMetric.getConditionalBranches());
	}

	@Test public void ifBranchMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("ifBranch()V"); 
		assertEquals(3, methodMetric.blocks().size());
		assertEquals(1, methodMetric.getConditionalBranches());
	}

	@Test public void ifElseIfBranchesMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("ifElseIfBranches()V"); 
		assertEquals(5, methodMetric.blocks().size());
		assertEquals(2, methodMetric.getConditionalBranches());
	}
	
	@Test public void shortCircuitBranchMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("shortCircuitBranch()Z");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMetric.blocks().size());
		assertEquals(2, methodMetric.getConditionalBranches());
	}
	
	@Test public void tryCatchBranchMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("tryCatchBranch()V");
		assertEquals(3, methodMetric.blocks().size());
		assertEquals(1, methodMetric.getConditionalBranches());
	}

	@Test public void tryFinallyBranchMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("tryFinallyBranch()V");
		assertEquals(3, methodMetric.blocks().size());
		assertEquals(1, methodMetric.getConditionalBranches());
	}

	@Test public void tryCatchFinallyBranchMethod() throws IOException {
		MethodMetric methodMetric = classMetric.getMethod("tryCatchFinallyBranch()V");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMetric.blocks().size());
		assertEquals(2, methodMetric.getConditionalBranches());
	}
}
