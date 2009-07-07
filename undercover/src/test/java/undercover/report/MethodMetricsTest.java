package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MethodMetricsTest {
	private List<MethodItem> methods;
	private BlockMetrics blockMetrics;
	private MethodMetrics dut;
	private MethodItem m1;
	private MethodItem m2;

	@Before public void beforeEach() {
		m1 = new MethodItem("m1()V", 1, 4, 0);
		m2 = new MethodItem("m2()V", 2, 2, 2);
		methods = new ArrayList<MethodItem>(Arrays.asList(m1, m2));
		blockMetrics = new BlockMetrics(methods);
		dut = new MethodMetrics(methods, blockMetrics);
	}
	
	@Test public void getCount() {
		assertEquals(2, dut.getCount());
	}
	
	@Test public void getMaximumComplexity() {
		assertEquals(m2.getBlockMetrics().getComplexity(), dut.getMaximumComplexity());
	}
	
	@Test public void getAverageComplexity() {
		assertEquals((double) (m1.getBlockMetrics().getComplexity() + m2.getBlockMetrics().getComplexity()) / 2, dut.getAverageComplexity(), 0.01);
	}

	@Test public void getVariance() {
		double v = Math.pow((m1.getBlockMetrics().getComplexity() - dut.getAverageComplexity()), 2);
		v += Math.pow((m2.getBlockMetrics().getComplexity() - dut.getAverageComplexity()), 2);
		v /= 2;
		assertEquals(v, dut.getVariance(), 0.01);
		assertEquals(Math.sqrt(v), dut.getStandardDeviation(), 0.01);
	}

	@Test public void getCoveredCount() {
		MethodItem m3 = new MethodItem("m3()V", 1, 0, 0);
		methods.add(m3);
		assertEquals(2, dut.getExecutableCount());
		assertEquals(1, dut.getCoveredCount());
	}
}
