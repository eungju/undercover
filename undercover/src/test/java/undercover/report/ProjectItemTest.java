package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProjectItemTest {
	private ProjectItem dut;

	@Before public void beforeEach() {
		dut = new ProjectItem("Project name");
		
		PackageItem p1 = new PackageItem("p1");
		ClassItem c1 = new ClassItem("p1/c1", new SourceFile("p1/c1.java"));
		c1.addMethod(new MethodItem("p1/c1.m1()V", 1, 2, 2));
		p1.addClass(c1);
		dut.addPackage(p1);
		
		PackageItem p2 = new PackageItem("p2");
		ClassItem c2 = new ClassItem("p2/c2", new SourceFile("p2/c2.java"));
		c2.addMethod(new MethodItem("p2/c2.m2()V", 2, 2, 1));
		p2.addClass(c2);
		dut.addPackage(p2);
	}
	
	@Test public void getDisplayName() {
		assertEquals("Project name", dut.getDisplayName());
	}
	
	@Test public void getComplexity() {
		assertEquals(3, dut.getComplexity());
	}

	@Test public void getBlockCount() {
		assertEquals(4, dut.getBlockCount());
	}

	@Test public void getCoveredBlockCount() {
		assertEquals(3, dut.getCoveredBlockCount());
	}
	
	@Test public void getMethodCount() {
		assertEquals(2, dut.getMethodCount());
	}

	@Test public void getAverageMethodComplexity() {
		assertEquals(1.5, dut.getAverageMethodComplexity(), 0.01);
	}

	@Test public void getMaximumMethodComplexity() {
		assertEquals(2, dut.getMaximumMethodComplexity());
	}
}
