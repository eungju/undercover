package undercover.report;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import undercover.data.BlockMeta;
import undercover.data.ClassMeta;
import undercover.data.MetaData;
import undercover.data.MethodMeta;
import undercover.runtime.Coverage;
import undercover.runtime.CoverageData;
import undercover.support.Proportion;

@RunWith(JMock.class)
public class ReportDataBuilderTest {
	private ReportDataBuilder dut;
	private CoverageData coverageData;
	private Mockery mockery = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	@Before public void beforeEach() {
		coverageData = new CoverageData();
		dut = new ReportDataBuilder(null, coverageData);
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
		dut.sourceItem = mockery.mock(SourceItem.class);
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		mockery.checking(new Expectations() {{
			one(dut.sourceItem).addBlock(block, 0);
		}});
		dut.classCoverage = null;
		block.accept(dut);
	}

	@Test public void visitBlockWithClassCoverage() {
		dut.sourceItem = mockery.mock(SourceItem.class);
		final BlockMeta block = new BlockMeta(Arrays.asList(1, 2, 3));
		mockery.checking(new Expectations() {{
			one(dut.sourceItem).addBlock(block, 1);
		}});
		dut.classCoverage = new Coverage("", new int[][] { {1} });
		block.accept(dut);
	}
}
