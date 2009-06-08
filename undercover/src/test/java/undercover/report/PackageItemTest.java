package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PackageItemTest {
	private PackageItem dut;

	@Before public void beforeEach() {
		dut = new PackageItem("p");
		ClassItem c1 = new ClassItem("p/c1", new SourceFile("p/c1.java"));
		c1.addMethod(new MethodItem("p/c1.m1()V", 1, 2, 2));
		dut.addClass(c1);
		ClassItem c2 = new ClassItem("p/c2", new SourceFile("p/c2.java"));
		c2.addMethod(new MethodItem("p/c2.m2()V", 2, 2, 1));
		dut.addClass(c2);
	}
	
	@Test public void getDisplayName() {
		assertEquals("p", dut.getDisplayName());
	}
	
	@Test public void getLinkName() {
		assertEquals("p", dut.getLinkName());
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
