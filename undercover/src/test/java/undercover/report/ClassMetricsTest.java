package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import undercover.support.Proportion;

public class ClassMetricsTest {
	private List<ClassItem> classes;
	private ClassMetrics dut;
	private ClassItem c1;
	private ClassItem c2;

	@Before public void beforeEach() {
		c1 = new ClassItem("p/c1");
		c1.addMethod(new MethodItem("m1()V", 1, 4, 0));
		
		c2 = new ClassItem("p/c2");
		c2.addMethod(new MethodItem("m2()V", 2, 2, 2));
		
		classes = new ArrayList<ClassItem>(Arrays.asList(c1, c2));
		dut = new ClassMetrics(classes);
	}
	
	@Test public void getCount() {
		assertEquals(2, dut.getCount());
	}
	
	@Test public void getComplexity() {
		dut.getComplexity();
	}
	
	@Test public void getCoverage() {
		ClassItem c3 = new ClassItem("p/c3");
		c3.addMethod(new MethodItem("m3()V", 1, 0, 0));
		classes.add(c3);
		assertEquals(new Proportion(1, 2), dut.getCoverage());
	}
}
