package undercover.instrument;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import undercover.metric.ClassMeta;
import undercover.metric.MetaData;
import undercover.metric.MethodMeta;

//FIXME: How to test instrumented bytecode?
public class InstrumentTest {
	private Instrument dut;
	private ClassMeta classMeta;
	
	public String traceBytecode(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		TraceClassVisitor trace = new TraceClassVisitor(writer);
		cr.accept(trace, 0);
		return buffer.toString();
	}

	public String checkBytecode(byte[] bytecode) {
		ClassReader cr = new ClassReader(bytecode);
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		CheckClassAdapter.verify(cr, true, writer);
		return buffer.toString();
	}

	@Before public void beforeEach() throws IOException {
		dut = new Instrument();
		byte[] original = IOUtils.toByteArray(getClass().getResourceAsStream("HelloWorld.class"));
		MetaData metaData = dut.getMetaData();
		checkBytecode(dut.instrument(original));
		classMeta = metaData.getClass(HelloWorld.class.getName().replaceAll("\\.", "/"));
	}
	
	@Test public void abstractMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("abstractMethod()V");
		assertEquals(0, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void block() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("b1()Z"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void simple() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("simple()V"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void sequential() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("sequential()V"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void ifBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("ifBranch()V"); 
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}

	@Test public void ifElseIfBranchesMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("ifElseIfBranches()V"); 
		assertEquals(5, methodMeta.blocks.size());
		assertEquals(3, methodMeta.complexity);
	}
	
	@Test public void shortCircuitBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("shortCircuitBranch()Z");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMeta.blocks.size());
		assertEquals(3, methodMeta.complexity);
	}
	
	@Test public void tryCatchBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryCatchBranch()V");
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void tryFinallyBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryFinallyBranch()V");
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void tryCatchFinallyBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryCatchFinallyBranch()V");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void forLoop() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("forLoop(I)V");
		assertEquals(4, methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}
}
