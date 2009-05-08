package undercover.report.html;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class HtmlReportTest {
	private HtmlReport dut;

	@Before public void beforeEach() throws IOException {
		dut = new HtmlReport();
	}
	
	@Test public void relativePath() {
		assertEquals("file", dut.getRelativeSourcePath(new File[] { new File("dir") }, new File("dir/file")));
		assertEquals("file", dut.getRelativeSourcePath(new File[] { new File("dir/") }, new File("dir/file")));
	}
}
