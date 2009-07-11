package undercover.support;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProportionTest {
	@Test public void ratio() {
		assertEquals(0.5, new Proportion(1, 2).getRatio(), 0);
	}
}
