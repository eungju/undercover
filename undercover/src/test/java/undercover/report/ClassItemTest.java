package undercover.report;

import static org.junit.Assert.*;

import org.junit.Test;

import undercover.metric.ClassMetric;

public class ClassItemTest {
	@Test public void normalClass() {
		ClassItem classItem = new ClassItem(null, new ClassMetric("p/c", "c.java"));
		assertEquals("p/c", classItem.getName());
		assertEquals("p.c", classItem.getDisplayName());
		assertEquals("c", classItem.simpleName);
	}
	
	@Test public void composition() {
		ClassItem classItem = new ClassItem(null, new ClassMetric("p/c", "c.java"));
		
		classItem.addMethod(new MethodItem(null, null, null, 1, 1));
		assertEquals(1, classItem.getBlockCount());
		assertEquals(1, classItem.getCoveredBlockCount());
		assertEquals(1, classItem.getMethodCount());
		
		classItem.addMethod(new MethodItem(null, null, null, 0, 0));
		assertEquals(1, classItem.getBlockCount());
		assertEquals(1, classItem.getCoveredBlockCount());
		assertEquals(2, classItem.getMethodCount());

		classItem.addMethod(new MethodItem(null, null, null, 2, 1));
		assertEquals(3, classItem.getBlockCount());
		assertEquals(2, classItem.getCoveredBlockCount());
		assertEquals(3, classItem.getMethodCount());
	}
}
