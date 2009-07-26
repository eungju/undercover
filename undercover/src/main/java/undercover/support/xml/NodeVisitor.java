package undercover.support.xml;

public interface NodeVisitor {
	void enterElement(Element node);
	void leaveElement(Element node);
	void visitText(Text node);
}
