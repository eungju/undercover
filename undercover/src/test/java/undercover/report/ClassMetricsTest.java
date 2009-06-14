package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClassMetricsTest {
	private List<ClassItem> classes;
	private BlockMetrics blockMetrics;
	private ClassMetrics dut;
	private ClassItem c1;
	private ClassItem c2;

	@Before public void beforeEach() {
		c1 = new ClassItem("p/c1", null);
		c1.addMethod(new MethodItem("m1()V", 1, 4, 2));
		
		c2 = new ClassItem("p/c2", null);
		c2.addMethod(new MethodItem("m2()V", 2, 2, 2));
		
		classes = Arrays.asList(c1, c2);
		blockMetrics = new BlockMetrics(classes);
		dut = new ClassMetrics(classes, blockMetrics);
	}
	
	@Test public void getCount() {
		assertEquals(2, dut.getCount());
	}
	
	@Test public void getMaximumComplexity() {
		assertEquals(c2.getBlockMetrics().getComplexity(), dut.getMaximumComplexity());
	}
	
	@Test public void getAverageComplexity() {
		assertEquals((double) (c1.getBlockMetrics().getComplexity() + c2.getBlockMetrics().getComplexity()) / 2, dut.getAverageComplexity(), 0.01);
	}
}
