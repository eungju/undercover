package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProjectItemTest {
	private ProjectItem dut;

	@Before public void beforeEach() {
		dut = new ProjectItem("Project name");
	}
	
	@Test public void getDisplayName() {
		assertEquals("Project name", dut.getDisplayName());
	}

	@Test public void metrics() {
		assertNotNull(dut.getBlockMetrics());
		assertNotNull(dut.getMethodMetrics());
		assertNotNull(dut.getClassMetrics());
		assertNotNull(dut.getPackageMetrics());
	}
}
