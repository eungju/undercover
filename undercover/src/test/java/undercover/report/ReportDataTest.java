package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import undercover.data.BlockMeta;
import undercover.data.ClassMeta;
import undercover.data.Coverage;
import undercover.data.CoverageData;
import undercover.data.MetaData;
import undercover.data.MethodMeta;
import undercover.support.UndercoverMockery;

@RunWith(JMock.class)
public class ReportDataTest {
	private ReportDataBuilder dut;
	private CoverageData coverageData;
	private Mockery mockery = new UndercoverMockery();

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
		assertNotNull(dut.classItems.get(classMeta.name));
		assertNotNull(dut.packageItems.get("pkg"));
		assertNotNull(dut.sourceItems.get("pkg/cls.java"));
	}
	
	@Test public void visitMethod() {
		BlockMeta b1 = new BlockMeta(new ArrayList<Integer>());
		MethodMeta methodMeta = new MethodMeta("m", "()V", 1, Arrays.asList(b1, new BlockMeta(new ArrayList<Integer>())));
		dut.classCoverage = new Coverage("p/c", new int[][] { {1, 0} });

		SourceFile sourceFile = new SourceFile("c.java");
		dut.classItem = new ClassItem("p/c", sourceFile);
		dut.sourceItem = new SourceItem(sourceFile);
		methodMeta.accept(dut);
		assertEquals(2, dut.methodItem.getBlockMetrics().getBlockCount());
		assertEquals(1, dut.methodItem.getBlockMetrics().getCoveredBlockCount());
	}
	
	@Test public void visitBlockWithoutClassCoverage() {
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		dut.sourceItem = mockery.mock(SourceItem.class);
		
		mockery.checking(new Expectations() {{
			one(dut.sourceItem).addBlock(block, 0);
		}});

		block.accept(dut);
	}

	@Test public void visitBlockWithClassCoverage() {
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		dut.sourceItem = mockery.mock(SourceItem.class);
		dut.classCoverage = new Coverage("", new int[][] { {1} });

		mockery.checking(new Expectations() {{
			one(dut.sourceItem).addBlock(block, 1);
		}});

		block.accept(dut);
	}
}
