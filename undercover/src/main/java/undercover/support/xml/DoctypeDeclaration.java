package undercover.support.xml;

public class DoctypeDeclaration implements Node {
	public final String name;
	public final String uriReference;
	public final String publicIdentifier;
	
	public DoctypeDeclaration(String name, String uriReference) {
		this(name, uriReference, null);
	}
	
	public DoctypeDeclaration(String name, String uriReference, String publicIdentifier) {
		this.name = name;
		this.uriReference = uriReference;
		this.publicIdentifier = publicIdentifier;
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.visitDoctypeDeclaration(this);
	}
}
