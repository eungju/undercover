package undercover.support.xml;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class XmlTest {
	@Test public void elementsHaveChildrenNodes() {
		Element child = new Element("a");
		Element element = new Element("div").append(child);
		assertEquals(Arrays.asList(child), element.children);
	}
	
	@Test public void elementsHaveAttributes() {
		Element element = new Element("a").attr("string", "#1").attr("int", 1).attr("long", 1L).attr("double", 1.0D);
		assertEquals(4, element.attributes.size());
	}
	
	@Test public void textsHaveValue() {
		assertEquals("foo", new Text("foo").value);
	}
}
