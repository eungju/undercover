package undercover.report;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import undercover.metric.ClassMeta;
import undercover.metric.CoverageData;
import undercover.metric.MetaData;

public class ReportDataTest {
	private MetaData metaData;
	private CoverageData coverageData;
	private ReportData dut;

	@Before public void beforeEach() {
		metaData = new MetaData();
		coverageData = new CoverageData();
		dut = new ReportData(metaData, coverageData, "foo", new SourceFinder(new ArrayList<File>()));
	}
	
	@Test public void addClass() {
		ClassMeta classMeta = new ClassMeta("pkg/cls", "cls.java");
		dut.addClass(classMeta);
		assertNotNull(dut.getClass(classMeta.name()));
		assertNotNull(dut.getPackage("pkg"));
	}
}
