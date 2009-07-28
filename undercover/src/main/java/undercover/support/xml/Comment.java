package undercover.support.xml;

public class Comment implements Node {
	public final String value;

	public Comment(String value) {
		this.value = value;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visitComment(this);
	}
}
