package undercover.maven.sample;

import static org.junit.Assert.*;

import org.junit.Test;

public class SampleTest {
	@Test public void hook() {
		assertTrue(1 == 1);
		assertNotNull(new Sample());
	}
	
	@Test public void xxx() {
		assertTrue(true);
	}
}
