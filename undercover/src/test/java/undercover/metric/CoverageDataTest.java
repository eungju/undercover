package undercover.metric;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

public class CoverageDataTest {
	@Test public void saveAndLoad() throws IOException {
		File file = File.createTempFile("undercover-", ".cd");
		CoverageData expected = new CoverageData();
		expected.touchBlock(UUID.randomUUID());
		expected.save(file);
		CoverageData actual = CoverageData.load(file);
		assertEquals(expected, actual);
	}
}
