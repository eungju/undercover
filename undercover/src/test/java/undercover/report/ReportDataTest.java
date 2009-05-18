package undercover.report;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.BlockMeta;
import undercover.metric.ClassMeta;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;
import undercover.metric.MethodMeta;

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
	
	@Test public void acceptBlock() {
		MethodMeta methodMeta = new MethodMeta("m()V");
		BlockMeta b1 = new BlockMeta();
		methodMeta.addBlock(b1);
		methodMeta.addBlock(new BlockMeta());
		coverageData.touchBlock(b1.id());

		dut.classItem = new ClassItem(null, "p/c", "c.java");
		methodMeta.accept(dut);
		assertEquals(2, dut.methodItem.getBlockCount());
		assertEquals(1, dut.methodItem.getCoveredBlockCount());
	}
}
