package undercover.report.html;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import undercover.report.MethodItem;

public class DashboardPageTest {
	private DashboardPage dut;

	@Before public void beforeEach() {
		dut = new DashboardPage(null);
	}
	
	@Test public void lowCoverageIsRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 1, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(Arrays.asList(m1, m2), 10));
	}

	@Test public void highComplexityRisky() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 1);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.mostRisky(Arrays.asList(m1, m2), 10));
	}
	
	@Test public void mostComplex() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 2);
		assertEquals(Arrays.asList(m2, m1), dut.mostComplex(Arrays.asList(m1, m2), 10));
	}

	@Test public void leastCovered() {
		MethodItem m1 = new MethodItem("a()V", 1, 2, 2);
		MethodItem m2 = new MethodItem("b()V", 2, 2, 1);
		assertEquals(Arrays.asList(m2, m1), dut.leastCovered(Arrays.asList(m1, m2), 10));
	}	
}
