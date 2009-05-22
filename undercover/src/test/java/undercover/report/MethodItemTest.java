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
}
