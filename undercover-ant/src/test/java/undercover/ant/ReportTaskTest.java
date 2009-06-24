package undercover.ant;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class ReportTaskTest {
	private ReportTask dut;

	@Before public void beforeEach() {
		dut = new ReportTask();
	}
	
	@Test public void sourcePathIsNotRequired() {
		dut.checkSourcePath();
		assertEquals(Collections.emptyList(), dut.sourcePaths);
	}

	@Test public void sourcePath() throws IOException {
		File dir = File.createTempFile("java", "");
		dut.createSourcePath().createPathElement().setLocation(dir);
		dut.checkSourcePath();
		assertEquals(Arrays.asList(dir), dut.sourcePaths);
	}
	
	@Test public void sourceEncoding() {
		dut.setSourceEncoding("UTF-8");
		dut.checkSourceEncoding();
		assertEquals("UTF-8", dut.sourceEncoding);
	}
	
	@Test public void sourceEncodingHasDefaultValue() {
		dut.checkSourceEncoding();
		assertEquals(Charset.defaultCharset().name(), dut.sourceEncoding);
	}
}
