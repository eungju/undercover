package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassItemTest {
	private ClassItem dut;

	@Before public void beforeEach() {
		dut = new ClassItem("p/c");
		dut.setSource(new SourceItem(new SourceFile("p/c.java")));
	}
	
	@Test public void getDisplayName() {
		assertEquals("p.c", dut.getDisplayName());
	}
	
	@Test public void getSimpleName() {
		assertEquals("c", dut.getSimpleName());
	}

	@Test public void metrics() {
		assertNotNull(dut.getBlockMetrics());
		assertNotNull(dut.getMethodMetrics());
		assertNull(dut.getClassMetrics());
		assertNull(dut.getPackageMetrics());
	}
}
