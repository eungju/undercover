package undercover.report.xml;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceFile;
import undercover.report.SourceItem;
import undercover.support.xml.Element;

public class CoberturaXmlReportTest {
	private CoberturaXmlReport dut;

	@Before public void beforeEach() {
		dut = new CoberturaXmlReport(null);
	}
	
	@Test public void buildCoverage() {
		Element actual = dut.buildCoverage(new ReportData("Project name", Collections.<PackageItem>emptySet(), Collections.<ClassItem>emptySet(), Collections.<SourceItem>emptySet()));
		assertEquals("coverage", actual.name);
		assertEquals("1.0", actual.attr("line-rate"));
		assertEquals("1.0", actual.attr("branch-rate"));
	}
	
	@Test public void buildPackages() {
		PackageItem item = new PackageItem("p");
		Element actual = dut.buildPackages(Arrays.asList(item));
		assertEquals("packages", actual.name);
		assertEquals(1, actual.children.size());
		actual = (Element) actual.children.get(0);
		assertEquals("package", actual.name);
		assertEquals(item.getDisplayName(), actual.attr("name"));
		assertEquals("1.0", actual.attr("line-rate"));
		assertEquals("1.0", actual.attr("branch-rate"));
		assertEquals("0.0", actual.attr("complexity"));
		assertEquals("classes", ((Element) actual.children.get(0)).name);
	}

	@Test public void buildClasses() {
		ClassItem item = new ClassItem("p/c");
		item.setSource(new SourceItem(new SourceFile("p/c.java")));
		Element actual = dut.buildClasses(Arrays.asList(item));
		assertEquals("classes", actual.name);
		assertEquals(1, actual.children.size());
		actual = (Element) actual.children.get(0);
		assertEquals("class", actual.name);
		assertEquals(item.getDisplayName(), actual.attr("name"));
		assertEquals("1.0", actual.attr("line-rate"));
		assertEquals("1.0", actual.attr("branch-rate"));
		assertEquals("0.0", actual.attr("complexity"));
		assertEquals("methods", ((Element) actual.children.get(0)).name);
		assertEquals("lines", ((Element) actual.children.get(1)).name);
	}

	@Test public void buildMethods() {
		MethodItem item = new MethodItem("m()V", 1, 2, 1);
		Element actual = dut.buildMethods(Arrays.asList(item));
		assertEquals("methods", actual.name);
		assertEquals(1, actual.children.size());
		actual = (Element) actual.children.get(0);
		assertEquals("method", actual.name);
		assertEquals("m", actual.attr("name"));
		assertEquals("()V", actual.attr("signature"));
		assertEquals("0.5", actual.attr("line-rate"));
		assertEquals("0.5", actual.attr("branch-rate"));
		assertEquals("lines", ((Element) actual.children.get(0)).name);
	}
}
