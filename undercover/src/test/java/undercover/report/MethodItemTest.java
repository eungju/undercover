package undercover.report;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import undercover.data.BlockMeta;
import undercover.data.MethodMeta;

public class MethodItemTest {
	@Test public void normalMethod() {
		MethodItem methodItem = new MethodItem(new MethodMeta("m", "()V", 0, Collections.<BlockMeta>emptyList()), 1);
		assertEquals("m()V", methodItem.getName());
		assertEquals("m() : void", methodItem.getDisplayName());
	}
	
	@Test public void isExecutable() {
		assertTrue(new MethodItem("m()V", 1, 1, 0).getBlockMetrics().isExecutable());
		assertFalse(new MethodItem("m()V", 1, 0, 0).getBlockMetrics().isExecutable());
	}
	
	@Test public void getCoverageRate() {
		assertEquals(0.5, new MethodItem("m()V", 1, 2, 1).getBlockMetrics().getCoverageRate(), 0.01);
		assertEquals(1, new MethodItem("m()V", 1, 2, 2).getBlockMetrics().getCoverageRate(), 0.01);

		assertEquals(1, new MethodItem("m()V", 1, 0, 0).getBlockMetrics().getCoverageRate(), 0.01);
	}

	@Test public void getRisk() {
		assertEquals(1, new MethodItem("m()V", 1, 2, 2).getBlockMetrics().getRisk(), 0.01);
		assertEquals(1 + 1, new MethodItem("m()V", 1, 2, 0).getBlockMetrics().getRisk(), 0.01);
	}
}
