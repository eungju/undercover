package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.ClassMeta;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;

public class ReportDataTest {
	private CoverageData coverageData;
	private ReportDataBuilder dut;

	@Before public void beforeEach() {
		coverageData = new CoverageData();
		dut = new ReportDataBuilder(coverageData);
		MetaData metaData = new MetaData();
		dut.visitEnter(metaData);
	}
	
	@Test public void analyzeClass() {
		ClassMeta classMeta = new ClassMeta("pkg/cls", "cls.java");
		classMeta.accept(dut);
		assertNotNull(dut.classItems.get(classMeta.name()));
		assertNotNull(dut.packageItems.get("pkg"));
	}
}
