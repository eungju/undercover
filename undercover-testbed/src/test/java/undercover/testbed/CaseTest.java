package undercover.testbed;

import static org.junit.Assert.*;

import org.junit.Test;

public class CaseTest {
	@Test public void failure() {
		assertTrue(false);
	}

	@Test public void error() {
		throw new IllegalStateException();
	}
}
