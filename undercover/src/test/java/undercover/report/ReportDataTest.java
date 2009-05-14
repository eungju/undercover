package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.BlockMetric;
import undercover.metric.ClassMetric;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;
import undercover.metric.MethodMetric;

public class ReportDataTest {
	private MetaData metaData;
	private CoverageData coverageData;
	private ReportData dut;

	@Before public void beforeEach() {
		metaData = new MetaData();
		coverageData = new CoverageData();
		dut = new ReportData(metaData, coverageData, "foo");
	}
	
	@Test public void addClass() {
		ClassMetric classMetric = new ClassMetric("pkg/cls", "cls.java");
		dut.addClass(classMetric);
		assertNotNull(dut.getClass(classMetric.name()));
		assertNotNull(dut.getPackage("pkg"));
	}
	
	@Test public void methodItem() {
		MethodMetric methodMetric = new MethodMetric("foo", "()V");
		BlockMetric b1 = new BlockMetric();
		methodMetric.addBlock(b1);
		methodMetric.addBlock(new BlockMetric());
		CoverageData coverageData = new CoverageData();
		coverageData.touchBlock(b1.id());
		ClassItem classItem = new ClassItem(null, new ClassMetric("p/c", "c.java"));
		MethodItem methodItem = new MethodItem(classItem, methodMetric, coverageData);
		assertEquals(2, methodItem.getBlockCount());
		assertEquals(1, methodItem.getCoveredBlockCount());
		assertEquals(2, methodItem.getComplexity());
		assertEquals(0.5, methodItem.getCoverageRate(), 0);
	}
}
