package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import undercover.support.Proportion;

public class MethodMetricsTest {
	private List<MethodItem> methods;
	private MethodMetrics dut;
	private MethodItem m1;
	private MethodItem m2;

	@Before public void beforeEach() {
		m1 = new MethodItem("m1()V", 1, 4, 0);
		m2 = new MethodItem("m2()V", 2, 2, 2);
		methods = new ArrayList<MethodItem>(Arrays.asList(m1, m2));
		dut = new MethodMetrics(methods);
	}
	
	@Test public void getCount() {
		assertEquals(2, dut.getCount());
	}
	
	@Test public void getComplexity() {
		dut.getComplexity();
	}

	@Test public void getCoverage() {
		MethodItem m3 = new MethodItem("m3()V", 1, 0, 0);
		methods.add(m3);
		assertEquals(new Proportion(1, 2), dut.getCoverage());
	}
}
