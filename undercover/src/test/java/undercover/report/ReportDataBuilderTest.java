package undercover.report;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import undercover.data.BlockMeta;
import undercover.data.ClassMeta;
import undercover.data.MetaData;
import undercover.data.MethodMeta;
import undercover.runtime.Coverage;
import undercover.runtime.CoverageData;
import undercover.support.Proportion;

public class ReportDataBuilderTest {
	private ReportDataBuilder dut;
	private CoverageData coverageData;

	@Before public void beforeEach() {
		coverageData = new CoverageData();
		dut = new ReportDataBuilder(coverageData);
	}
	
	@Test public void visitMetaData() {
		MetaData metaData = new MetaData();
		metaData.accept(dut);
	}
	
	@Test public void visitClass() {
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
		dut.classItem = new ClassItem("p/c");
		dut.sourceItem = new SourceItem(sourceFile);
		dut.classItem.setSource(dut.sourceItem);
		methodMeta.accept(dut);
		assertEquals(new Proportion(1, 2), dut.methodItem.getBlockMetrics().getCoverage());
	}
	
	@Test public void visitBlockWithoutClassCoverage() {
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		dut.sourceItem = mock(SourceItem.class);
		
		block.accept(dut);

		verify(dut.sourceItem).addBlock(block, 0);
	}

	@Test public void visitBlockWithClassCoverage() {
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		dut.sourceItem = mock(SourceItem.class);
		dut.classCoverage = new Coverage("", new int[][] { {1} });

		block.accept(dut);

		verify(dut.sourceItem).addBlock(block, 1);
	}
}
