package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.MethodMeta;

public class MethodItemTest {
	private ClassItem classItem;
	
	@Before public void beforeEach() {
		classItem = new ClassItem("p/c", new SourceFile("p/c.java"));
	}
	
	@Test public void normalMethod() {
		MethodItem methodItem = new MethodItem(classItem, new MethodMeta("m()V", 0), 1);
		assertEquals("p/c.m()V", methodItem.getName());
		assertEquals("p.c.m()V", methodItem.getDisplayName());
		assertEquals("m()V", methodItem.getSimpleName());
	}
	
	@Test public void isExecutable() {
		assertTrue(new MethodItem("p/c.m()V", 1, 1, 0).getBlockMetrics().isExecutable());
		assertFalse(new MethodItem("p/c.m()V", 1, 0, 0).getBlockMetrics().isExecutable());
	}
	
	@Test public void getCoverageRate() {
		assertEquals(0.5, new MethodItem("p/c.m()V", 1, 2, 1).getBlockMetrics().getCoverageRate(), 0.01);
		assertEquals(1, new MethodItem("p/c.m()V", 1, 2, 2).getBlockMetrics().getCoverageRate(), 0.01);

		assertEquals(1, new MethodItem("p/c.m()V", 1, 0, 0).getBlockMetrics().getCoverageRate(), 0.01);
	}

	@Test public void getRisk() {
		assertEquals(1, new MethodItem("p/c.m()V", 1, 2, 2).getBlockMetrics().getRisk(), 0.01);
		assertEquals(1 + 1, new MethodItem("p/c.m()V", 1, 2, 0).getBlockMetrics().getRisk(), 0.01);
	}
}
