package undercover.support.xml;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

public class XmlWriterTest {
	@Test public void integration() {
		StringWriter buffer = new StringWriter();
		PrintWriter writer = new PrintWriter(buffer);
		new Element("div").append(new Element("a").attr("href", "#1").append(new Text("foo"))).accept(new XmlWriter(writer));
		System.out.println(buffer.toString());
	}
}
