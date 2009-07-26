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
	
	public void enterElement(Element node) {
		if (onElement) {
			newline();
		}
		out.append('<').append(node.name);
		for (NameValuePair each : node.attributes) {
			out.append(' ').append(each.name);
			out.append("=\"").append(HtmlUtils.escape(each.value)).append('"');
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
