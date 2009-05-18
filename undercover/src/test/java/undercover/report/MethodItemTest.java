package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.BlockMeta;
import undercover.metric.CoverageData;
import undercover.metric.MethodMeta;

public class MethodItemTest {
	private ClassItem classItem;
	private CoverageData coverageData;
	
	@Before public void beforeEach() {
		classItem = new ClassItem(null, "p/c", "p/c.java");
		coverageData = new CoverageData();
	}
	
	@Test public void normalMethod() {
		MethodItem methodItem = new MethodItem(classItem, new MethodMeta("m()V"), coverageData);
		assertEquals("m()V", methodItem.getName());
		assertEquals("p.c.m()V", methodItem.getDisplayName());
	}
	
	@Test public void composition() {
		MethodMeta methodMeta = new MethodMeta("m()V");
		BlockMeta b1 = new BlockMeta();
		methodMeta.addBlock(b1);
		methodMeta.addBlock(new BlockMeta());
		coverageData.touchBlock(b1.id());
		MethodItem methodItem = new MethodItem(classItem, methodMeta, coverageData);
		assertEquals(2, methodItem.getBlockCount());
		assertEquals(1, methodItem.getCoveredBlockCount());
	}
}
