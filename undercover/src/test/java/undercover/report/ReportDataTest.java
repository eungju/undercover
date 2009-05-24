package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
	}
	
	@Test public void visitMetaData() {
		MetaData metaData = new MetaData();
		metaData.accept(dut);
	}
	
	@Test public void visitClass() {
		dut.projectItem = new ProjectItem("prj");
		ClassMeta classMeta = new ClassMeta("pkg/cls", "cls.java");
		classMeta.accept(dut);
		assertNotNull(dut.classItems.get(classMeta.name()));
		assertNotNull(dut.packageItems.get("pkg"));
		assertNotNull(dut.sourceItems.get("pkg/cls.java"));
	}
	
	@Test public void visitMethod() {
		MethodMeta methodMeta = new MethodMeta("m()V", 1);
		BlockMeta b1 = new BlockMeta(new ArrayList<Integer>());
		methodMeta.addBlock(b1);
		methodMeta.addBlock(new BlockMeta(new ArrayList<Integer>()));
		coverageData.register("p/c", new int[][] { {1, 0} });

		dut.classItem = new ClassItem("p/c", new SourceFile("c.java"));
		dut.sourceItem = new SourceItem("c.java", null);
		methodMeta.accept(dut);
		assertEquals(2, dut.methodItem.getBlockCount());
		assertEquals(1, dut.methodItem.getCoveredBlockCount());
	}
}
