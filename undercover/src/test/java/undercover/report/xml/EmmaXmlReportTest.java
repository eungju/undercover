package undercover.report.xml;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceFile;
import undercover.report.SourceItem;
import undercover.support.Proportion;
import undercover.support.xml.Element;

public class EmmaXmlReportTest {
	private EmmaXmlReport dut;

	@Before public void beforeEach() {
		dut = new EmmaXmlReport(null);
	}

	@Test public void buildReport() {
		ReportData item = new ReportData("cool product", Collections.<PackageItem>emptyList(), Collections.<ClassItem>emptyList(), Collections.<SourceItem>emptyList());
		Element actual = dut.buildReport(item);
		assertEquals("report", actual.name);
		assertEquals("stats", ((Element) actual.children.get(0)).name);
		assertEquals("data", ((Element) actual.children.get(1)).name);
	}

	@Test public void buildPackage() {
		PackageItem item = new PackageItem("p");
		Element actual = dut.buildPackage(item);
		assertEquals("package", actual.name);
		assertEquals(item.getDisplayName(), actual.attr("name"));
		assertEquals("coverage", ((Element) actual.children.get(0)).name);
		assertEquals("coverage", ((Element) actual.children.get(1)).name);
		assertEquals("coverage", ((Element) actual.children.get(2)).name);
	}

	@Test public void buildSourceFile() {
		SourceItem item = new SourceItem(new SourceFile("p/c.java"));
		Element actual = dut.buildSource(item);
		assertEquals("srcfile", actual.name);
		assertEquals(item.getSimpleName(), actual.attr("name"));
		assertEquals("coverage", ((Element) actual.children.get(0)).name);
		assertEquals("coverage", ((Element) actual.children.get(1)).name);
		assertEquals("coverage", ((Element) actual.children.get(2)).name);
	}

	@Test public void buildClass() {
		ClassItem item = new ClassItem("p/c");
		Element actual = dut.buildClass(item);
		assertEquals("class", actual.name);
		assertEquals(item.getSimpleName(), actual.attr("name"));
		assertEquals("coverage", ((Element) actual.children.get(0)).name);
		assertEquals("coverage", ((Element) actual.children.get(1)).name);
		assertEquals("coverage", ((Element) actual.children.get(2)).name);
	}
	
	@Test public void buildMethod() {
		MethodItem item = new MethodItem("m()V", 1, 2, 0);
		Element actual = dut.buildMethod(item);
		assertEquals("method", actual.name);
		assertEquals(item.getDisplayName(), actual.attr("name"));
		assertEquals("coverage", ((Element) actual.children.get(0)).name);
		assertEquals("coverage", ((Element) actual.children.get(1)).name);
	}
	
	@Test public void buildCoverage() {
		Element actual = dut.buildCoverage("block", new Proportion(1, 2));
		assertEquals("coverage", actual.name);
		assertEquals("block, %", actual.attr("type"));
		assertEquals("50% (1/2)", actual.attr("value"));
	}
}
