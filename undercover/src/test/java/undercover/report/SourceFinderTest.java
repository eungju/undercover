package undercover.report;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import undercover.metric.ClassMeta;

public class SourceFinderTest {
	private SourceFinder dut;
	private File src;
	
	@Before public void beforeEach() throws IOException {
		src = File.createTempFile("src", "");
		src.delete();
		src.mkdir();
		dut = new SourceFinder(Arrays.asList(src));
	}
	
	@After public void afterEach() throws IOException {
		FileUtils.deleteDirectory(src);
	}
	
	@Test public void notExist() throws IOException {
		assertNull(dut.findSourceFile(new ClassMeta("p/c", "c.java")));
		assertNull(dut.findSourcePath(new ClassMeta("p/c", "c.java")));
	}

	@Test public void existOnExpectedPath() throws IOException {
		File file = new File(src, "p/c.java");
		FileUtils.touch(file);
		assertEquals(file, dut.findSourceFile(new ClassMeta("p/c", "c.java")));
		assertEquals("p/c.java", dut.findSourcePath(new ClassMeta("p/c", "c.java")));
	}
}
