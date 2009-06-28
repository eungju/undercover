package undercover.ant;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class UndercoverTaskTest {
	private UndercoverTask dut;

	@Before public void beforeEach() {
		dut = new UndercoverTask() {
		};
	}
	
	@Test public void metaDataFile() {
		File file = new File("x.md");
		dut.setMetaDataFile(file);
		dut.checkMetaDataFile();
		assertEquals(file, dut.metaDataFile);
	}
	
	@Test public void metaDataFileHasDefault() {
		dut.checkMetaDataFile();
		assertEquals(new File("undercover.md"), dut.metaDataFile);
	}

	@Test public void coverageDataFile() {
		File file = new File("x.cd");
		dut.setCoverageDataFile(file);
		dut.checkCoverageDataFile();
		assertEquals(file, dut.coverageDataFile);
	}
	
	@Test public void coverageDataFileHasDefault() {
		dut.checkCoverageDataFile();
		assertEquals(new File("undercover.cd"), dut.coverageDataFile);
	}
}
