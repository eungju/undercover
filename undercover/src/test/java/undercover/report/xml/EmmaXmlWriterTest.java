package undercover.report.xml;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import undercover.support.Proportion;

public class EmmaXmlWriterTest {
	private StringWriter buffer;
	private EmmaXmlWriter dut;

	@Before public void beforeEach() {
		buffer = new StringWriter();
		dut = new EmmaXmlWriter(new PrintWriter(buffer), "UTF-8");
	}
	
	@After public void afterEach() {
		dut.close();
	}

	@Test public void reportElement() {
		dut.report().endReport();
	}

	@Test public void statsElement() {
		dut.stats().endStats();
	}

	@Test public void dataElement() {
		dut.data().endData();
	}

	@Test public void allElement() {
		dut.all().endAll();
	}

	@Test public void packageElement() {
		dut.package_("x.y").endPackage();
	}

	@Test public void sourceFileElement() {
		dut.sourceFile("Foo.java").endSourceFile();
	}

	@Test public void classElement() {
		dut.class_("Foo").endClass();
	}

	@Test public void methodElement() {
		dut.method("foo(): void").endMethod();
	}

	@Test public void coverageElement() {
		dut.coverage("block", new Proportion(1, 2));
	}
}
