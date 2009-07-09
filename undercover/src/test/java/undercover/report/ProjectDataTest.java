package undercover.report;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class ProjectDataTest {
	private ReportData dut;

	@Before public void beforeEach() {
		dut = new ReportData("Project name", Collections.<PackageItem>emptySet(), Collections.<ClassItem>emptySet(), Collections.<SourceItem>emptySet());
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
