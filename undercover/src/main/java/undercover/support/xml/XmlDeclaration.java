package undercover.support.xml;

public class XmlDeclaration implements Node {
	public final String version;
	public final String encoding;
	
	public XmlDeclaration(String version, String encoding) {
		this.version = version;
		this.encoding = encoding;
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.visitXmlDeclaration(this);
	}
}
