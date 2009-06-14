package undercover.report.html;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.SourceFile;

public class CoverageComplexityTest {
	private ClassItem c1;

	@Before public void beforeEach() {
		c1 = new ClassItem("HelloWorld");
		c1.setSourceFile(new SourceFile("p/c.java"));
	}
	
	@Test public void addAll() {
		CoverageComplexity cc = new CoverageComplexity();
		cc.addAll(Arrays.asList(c1));
	}

	@Test public void add() {
		CoverageComplexity cc = new CoverageComplexity();
		cc.add(c1);
	}
}
