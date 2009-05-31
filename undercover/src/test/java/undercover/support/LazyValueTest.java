package undercover.support;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LazyValueTest {
	private int count;

	LazyValue<Integer> lazy = new LazyValue<Integer>() {
		protected Integer calculate() {
			count++;
			return 3 + 4;
		}
	};

	@Before public void beforeEach() {
		count = 0;
	}
	
	@Test public void shouldNotCalculateTheValueTwice() {
		assertEquals(7, (int) lazy.value());
		assertEquals(7, (int) lazy.value());
		assertEquals(1, count);
	}

	@Test public void shouldCalculateAgainWhenForgot() {
		assertEquals(7, (int) lazy.value());
		lazy.forget();
		assertEquals(7, (int) lazy.value());
		assertEquals(2, count);
	}
}
