package undercover.support.xml;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class XmlWriterTest {
	private StringWriter buffer;
	private PrintWriter writer;
	private XmlWriter dut;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	@Before public void beforeEach() {
		buffer = new StringWriter();
		writer = new PrintWriter(buffer);
		dut = new XmlWriter(writer);
	}
	
	@Test public void xmlDeclaration() {
		new XmlDeclaration("1.0", "UTF-8").accept(dut);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SEPARATOR, buffer.toString());
	}

	@Test public void doctypeDeclaration() {
		new DoctypeDeclaration("root", "root.dtd").accept(dut);
		assertEquals("<!DOCTYPE root SYSTEM \"root.dtd\">" + LINE_SEPARATOR, buffer.toString());
	}

	@Test public void comment() {
		new Comment("This is a comment").accept(dut);
		assertEquals("<!-- This is a comment -->", buffer.toString());
	}

	@Test public void integration() {
		new Element("div").append(new Element("a").attr("href", "#1").append(new Text("foo"))).accept(dut);
		System.out.println(buffer.toString());
	}
}
