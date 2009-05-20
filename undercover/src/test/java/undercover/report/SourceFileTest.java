package undercover.report;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class SourceFileTest {
	@Test public void createWithFile() {
		File root = new File("a");
		File file = new File(root, "b");
		SourceFile dut = new SourceFile(root, file);
		assertEquals(root, dut.root);
		assertEquals("b", dut.path);
		assertEquals(file, dut.file);
	}

	@Test public void createWithPath() {
		File root = new File("a");
		SourceFile dut = new SourceFile(root, "b/c");
		assertEquals(root, dut.root);
		assertEquals("b/c", dut.path);
		assertEquals(new File(root, dut.path), dut.file);
	}

	@Test public void createWithoutRoot() {
		SourceFile dut = new SourceFile("b/c");
		assertEquals("b/c", dut.path);
		assertFalse(dut.isExist());
	}
}
