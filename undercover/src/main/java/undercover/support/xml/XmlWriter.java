package undercover.support.xml;

import java.io.PrintWriter;

public class XmlWriter implements NodeVisitor {
	private final PrintWriter out;
	private boolean onElement;
	private int depth;
	
	public XmlWriter(PrintWriter out) {
		this.out = out;
		onElement = false;
		depth = 0;
	}
	
	public void visitXmlDeclaration(XmlDeclaration node) {
		out.format("<?xml version=\"%s\" encoding=\"%s\"?>", node.version, node.encoding).println();
	}
	
	public void visitDoctypeDeclaration(DoctypeDeclaration node) {
		if (node.publicIdentifier == null) {
			out.format("<!DOCTYPE %s SYSTEM \"%s\">", node.name, node.uriReference).println();
		} else {
			out.format("<!DOCTYPE %s PUBLIC \"%s\" \"%s\">", node.name, node.publicIdentifier, node.uriReference).println();
		}
	}
	
	public void enterElement(Element node) {
		if (onElement) {
			newline();
		}
		out.append('<').append(node.name);
		for (NameValuePair each : node.attributes) {
			out.append(' ').append(each.name);
			out.append("=\"").append(XmlUtils.escape(each.value)).append('"');
		}
		out.append(">");
		onElement = true;
		depth++;
	}
	
	public void leaveElement(Element node) {
		depth--;
		if (onElement) {
			newline();
		}
		out.append("</").append(node.name).append(">");
		onElement = true;
	}

	public void visitText(Text node) {
		out.print(XmlUtils.escape(node.value));
		onElement = false;
	}

	public void visitCdata(Cdata node) {
		out.append("<![CDATA[").append(node.value).append("]]>");
		onElement = false;
	}

	public void visitComment(Comment node) {
		if (onElement) {
			newline();
		}
		out.append("<!--").append(node.value).append("-->");
		onElement = true;
	}

	void newline() {
		out.println();
		for (int i = 0; i < depth; i++) {
			out.print('\t');
		}
	}
}
