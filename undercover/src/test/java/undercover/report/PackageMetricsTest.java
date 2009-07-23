package undercover.report;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PackageMetricsTest {
	private List<PackageItem> packages;
	private PackageMetrics dut;
	private PackageItem p1;
	private PackageItem p2;

	@Before public void beforeEach() {
		p1 = new PackageItem("p1");
		ClassItem c1 = new ClassItem("p1/c1");
		c1.addMethod(new MethodItem("m1()V", 1, 4, 2));
		p1.addClass(c1);
		
		p2 = new PackageItem("p2");
		ClassItem c2 = new ClassItem("p2/c2");
		c2.addMethod(new MethodItem("m2()V", 2, 2, 2));
		p2.addClass(c2);
		
		packages = Arrays.asList(p1, p2);
		dut = new PackageMetrics(packages);
	}
	
	@Test public void getCount() {
		assertEquals(2, dut.getCount());
	}
	
	@Test public void getComplexity() {
		dut.getComplexity();
	}
}
