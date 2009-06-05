package undercover.report;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class ReportOutputTest {
	private ReportOutput dut;
	private File directory;

	@Before public void beforeEach() throws IOException {
		directory = File.createTempFile("undercover", "");
		directory.delete();
		dut = new ReportOutput(directory);
	}
	
	@Test public void writeInputStream() throws IOException {
		InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
		dut.write("file.ext", input);
		input.close();
		assertTrue(new File(directory, "file.ext").exists());
	}
	
	@Test public void writeString() throws IOException {
		String input = "hello";
		dut.write("file.ext", input);
		assertTrue(new File(directory, "file.ext").exists());
	}
}
