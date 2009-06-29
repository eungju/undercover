package undercover.support;

import static org.junit.Assert.*;

import org.junit.Test;

public class HtmlUtilsTest {
	@Test public void escape() {
		assertEquals(" &amp;&lt;&gt;&quot;", HtmlUtils.escape(" &<>\""));
	}
}
