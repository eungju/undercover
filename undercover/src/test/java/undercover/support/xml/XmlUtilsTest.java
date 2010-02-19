package undercover.support.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlUtilsTest {
	@Test public void escape() {
		assertEquals(" &amp;&lt;&gt;&quot;", XmlUtils.escape(" &<>\""));
	}
}
