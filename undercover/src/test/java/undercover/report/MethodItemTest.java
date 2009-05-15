package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.BlockMetric;
import undercover.metric.ClassMetric;
import undercover.metric.CoverageData;
import undercover.metric.MethodMetric;

public class MethodItemTest {
	private ClassItem classItem;
	private CoverageData coverageData;
	
	@Before public void beforeEach() {
		classItem = new ClassItem(null, new ClassMetric("p/c", "c.java"));
		coverageData = new CoverageData();
	}
	
	@Test public void normalMethod() {
		MethodItem methodItem = new MethodItem(classItem, new MethodMetric("m()V"), coverageData);
		assertEquals("m()V", methodItem.getName());
		assertEquals("p.c.m()V", methodItem.getDisplayName());
	}
	
	@Test public void composition() {
		MethodMetric methodMetric = new MethodMetric("m()V");
		BlockMetric b1 = new BlockMetric();
		methodMetric.addBlock(b1);
		methodMetric.addBlock(new BlockMetric());
		coverageData.touchBlock(b1.id());
		MethodItem methodItem = new MethodItem(classItem, methodMetric, coverageData);
		assertEquals(2, methodItem.getBlockCount());
		assertEquals(1, methodItem.getCoveredBlockCount());
	}
}
