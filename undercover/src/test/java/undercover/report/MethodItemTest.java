package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MethodItemTest {
	private ClassItem classItem;
	
	@Before public void beforeEach() {
		classItem = new ClassItem(null, "p/c", "p/c.java");
	}
	
	@Test public void normalMethod() {
		MethodItem methodItem = new MethodItem(classItem, "m()V", 0, 0, 0);
		assertEquals("m()V", methodItem.getName());
		assertEquals("p.c.m()V", methodItem.getDisplayName());
	}
}
