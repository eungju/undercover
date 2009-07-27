package undercover.support.xml;

public class DoctypeDeclaration implements Node {
	public final String name;
	public final String uriReference;
	
	public DoctypeDeclaration(String name, String uriReference) {
		this.name = name;
		this.uriReference = uriReference;
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.visitDoctypeDeclaration(this);
	}
}
