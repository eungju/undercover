package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.ClassMetric;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;

public class ReportDataTest {
	private MetaData metaData;
	private CoverageData coverageData;
	private ReportData dut;

	@Before public void beforeEach() {
		metaData = new MetaData();
		coverageData = new CoverageData();
		dut = new ReportData(metaData, coverageData);
	}
	
	@Test public void addClass() {
		ClassMetric classMetric = new ClassMetric("pkg/cls", "cls.java");
		dut.addClass(classMetric);
		assertNotNull(dut.getClass(classMetric.name()));
		assertNotNull(dut.getPackage("pkg"));
	}
}
