package undercover.instrument;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import undercover.data.ClassMeta;
import undercover.data.MetaData;
import undercover.data.MethodMeta;
import undercover.support.IOUtils;

//FIXME: How to test instrumented bytecode?
public class InstrumentTest {
	private Instrument dut;
	private ClassMeta classMeta;
	
	@Before public void beforeEach() throws IOException {
		dut = new Instrument();
		byte[] original = IOUtils.toByteArray(getClass().getResourceAsStream("HelloWorld.class"));
		MetaData metaData = dut.getMetaData();
		dut.instrument(original);
		classMeta = metaData.getClass(HelloWorld.class.getName().replaceAll("\\.", "/"));
	}
	
	@Test public void abstractMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("abstractMethod", "()V");
		assertEquals(0, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void block() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("b1", "()Z"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void simple() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("simple", "()V"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void sequential() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("sequential", "()V"); 
		assertEquals(1, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void ifBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("ifBranch", "()V"); 
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}

	@Test public void ifElseIfBranchesMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("ifElseIfBranches", "()V"); 
		assertEquals(5, methodMeta.blocks.size());
		assertEquals(3, methodMeta.complexity);
	}
	
	@Test public void shortCircuitBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("shortCircuitBranch", "()Z");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMeta.blocks.size());
		assertEquals(3, methodMeta.complexity);
	}
	
	@Test public void tryCatchBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryCatchBranch", "()V");
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}

	@Test public void tryFinallyBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryFinallyBranch", "()V");
		assertEquals(3, methodMeta.blocks.size());
		assertEquals(1, methodMeta.complexity);
	}

	@Test public void tryCatchFinallyBranchMethod() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("tryCatchFinallyBranch", "()V");
		//FIXME: Optimizer can create extra block.
		assertTrue(4 <= methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}

	@Test public void forLoop() throws IOException {
		MethodMeta methodMeta = classMeta.getMethod("forLoop", "(I)V");
		assertEquals(4, methodMeta.blocks.size());
		assertEquals(2, methodMeta.complexity);
	}
}
