package undercover.support.xml;

public class Cdata implements Node {
	public final String value;

	public Cdata(String value) {
		this.value = value;
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.visitCdata(this);
	}
}
