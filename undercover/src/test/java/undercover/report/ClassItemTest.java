package undercover.report;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassItemTest {
	@Test public void normalClass() {
		ClassItem classItem = new ClassItem("p/c", "p/c.java");
		assertEquals("p/c", classItem.getName());
		assertEquals("p.c", classItem.getDisplayName());
		assertEquals("c", classItem.simpleName);
	}
	
	@Test public void composition() {
		ClassItem classItem = new ClassItem("p/c", "p/c.java");
		
		classItem.addMethod(new MethodItem("p/c.m1()V", 1, 1, 1));
		assertEquals(1, classItem.getBlockCount());
		assertEquals(1, classItem.getCoveredBlockCount());
		assertEquals(1, classItem.getMethodCount());
		
		classItem.addMethod(new MethodItem("p/c.m2()V", 0, 0, 0));
		assertEquals(1, classItem.getBlockCount());
		assertEquals(1, classItem.getCoveredBlockCount());
		assertEquals(2, classItem.getMethodCount());

		classItem.addMethod(new MethodItem("p/c.m3()V", 1, 2, 1));
		assertEquals(3, classItem.getBlockCount());
		assertEquals(2, classItem.getCoveredBlockCount());
		assertEquals(3, classItem.getMethodCount());
	}
}
