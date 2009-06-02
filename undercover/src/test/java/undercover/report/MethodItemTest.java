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
	}
	
	@Test public void isExecutable() {
		assertTrue(new MethodItem("p/c.m()V", 1, 1, 0).isExecutable());
		assertFalse(new MethodItem("p/c.m()V", 1, 0, 0).isExecutable());
	}
	
	@Test public void getCoverageRate() {
		assertEquals(0.5, new MethodItem("p/c.m()V", 1, 2, 1).getCoverageRate(), 0.01);
		assertEquals(1, new MethodItem("p/c.m()V", 1, 2, 2).getCoverageRate(), 0.01);

		assertEquals(1, new MethodItem("p/c.m()V", 1, 0, 0).getCoverageRate(), 0.01);
	}

	@Test public void getRisk() {
		assertEquals(1, new MethodItem("p/c.m()V", 1, 2, 2).getRisk(), 0.01);
		assertEquals(1 + 1, new MethodItem("p/c.m()V", 1, 2, 0).getRisk(), 0.01);
	}
}
