package undercover.runtime;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import undercover.runtime.CoverageData;

public class CoverageDataTest {
	@Test public void saveAndLoad() throws IOException {
		File file = File.createTempFile("undercover-", ".cd");
		CoverageData expected = new CoverageData();
		expected.register("p/c", new int[3][4]);
		expected.save(file);
		CoverageData actual = CoverageData.load(file);
		assertNotNull(actual.getCoverage("p/c"));
	}
}
