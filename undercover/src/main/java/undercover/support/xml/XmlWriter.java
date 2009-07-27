package undercover.support.xml;

import java.io.PrintWriter;

import undercover.support.HtmlUtils;

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
		out.format("<!DOCTYPE %s SYSTEM \"%s\">", node.name, node.uriReference).println();
	}
	
	public void enterElement(Element node) {
		if (onElement) {
			newline();
		}
		out.append('<').append(node.name);
		for (NameValuePair each : node.attributes) {
			out.append(' ').append(each.name);
			out.append("=\"").append(HtmlUtils.escape(each.value)).append('"');
		}
		if (node.children.isEmpty()) {
			out.append(" />");
		} else {
			out.append(">");
			onElement = true;
			depth++;
		}
	}
	
	public void leaveElement(Element node) {
		if (node.children.isEmpty()) {
			return;
		}
		
		depth--;
		if (onElement) {
			newline();
		}
		out.append("</").append(node.name).append(">");
		onElement = true;
	}

	public void visitText(Text node) {
		out.print(HtmlUtils.escape(node.value));
		onElement = false;
	}

	void newline() {
		out.println();
		for (int i = 0; i < depth; i++) {
			out.print('\t');
		}
	}
}
