package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import undercover.support.LazyValue;

public class ComplexityStatisticsTest {
	private List<MethodItem> methods;
	private ComplexityStatistics dut;
	private MethodItem m1;
	private MethodItem m2;

	@Before public void beforeEach() {
		methods = new ArrayList<MethodItem>();
		dut = new ComplexityStatistics(new LazyValue<Collection<MethodItem>>() {
			@Override
			protected Collection<MethodItem> calculate() {
				return methods;
			}
		});
		m1 = new MethodItem("m1()V", 1, 4, 0);
		m2 = new MethodItem("m2()V", 2, 2, 2);
		methods.add(m1);
		methods.add(m2);
	}
	
	@Test public void getMaximumComplexity() {
		assertEquals(m2.getBlockMetrics().getComplexity(), dut.getMaximum());
	}
	
	@Test public void getAverageComplexity() {
		assertEquals((double) (m1.getBlockMetrics().getComplexity() + m2.getBlockMetrics().getComplexity()) / 2, dut.getAverage(), 0);
	}

	@Test public void getComplexityVariance() {
		double v = Math.pow((m1.getBlockMetrics().getComplexity() - dut.getAverage()), 2);
		v += Math.pow((m2.getBlockMetrics().getComplexity() - dut.getAverage()), 2);
		v /= 2;
		assertEquals(v, dut.getVariance(), 0);
		assertEquals(Math.sqrt(v), dut.getStandardDeviation(), 0);
	}
}
