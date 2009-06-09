package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassItemTest {
	private ClassItem dut;

	@Before public void beforeEach() {
		dut = new ClassItem("p/c", new SourceFile("p/c.java"));
		dut.addMethod(new MethodItem("m1()V", 1, 2, 2));
		dut.addMethod(new MethodItem("m2()V", 2, 2, 1));
	}
	
	@Test public void getDisplayName() {
		assertEquals("p.c", dut.getDisplayName());
	}
	
	@Test public void getLinkName() {
		assertEquals("p.c.java", dut.getLinkName());
	}
	
	@Test public void getSimpleName() {
		assertEquals("c", dut.getSimpleName());
	}

	@Test public void getComplexity() {
		assertEquals(3, dut.getBlockMetrics().getComplexity());
	}

	@Test public void getBlockCount() {
		assertEquals(4, dut.getBlockMetrics().getBlockCount());
	}

	@Test public void getCoveredBlockCount() {
		assertEquals(3, dut.getBlockMetrics().getCoveredBlockCount());
	}
	
	@Test public void getMethodCount() {
		assertEquals(2, dut.getMethodMetrics().getCount());
	}

	@Test public void getAverageMethodComplexity() {
		assertEquals(1.5, dut.getMethodMetrics().getAverageComplexity(), 0.01);
	}

	@Test public void getMaximumMethodComplexity() {
		assertEquals(2, dut.getMethodMetrics().getMaximumComplexity());
	}
}
