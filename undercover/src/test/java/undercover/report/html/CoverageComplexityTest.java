package undercover.report.html;

import java.util.Arrays;

import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.SourceFile;

public class CoverageComplexityTest {
	@Test public void addAll() {
		CoverageComplexity cc = new CoverageComplexity();
		cc.addAll(Arrays.asList(new ClassItem("HelloWorld", new SourceFile("p/c.java"))));
	}

	@Test public void add() {
		CoverageComplexity cc = new CoverageComplexity();
		cc.add(new ClassItem("HelloWorld", new SourceFile("p/c.java")));
	}
}
