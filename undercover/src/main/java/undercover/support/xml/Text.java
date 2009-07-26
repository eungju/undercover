package undercover.support.xml;

public class Text implements Node {
	public final String value;

	public Text(String value) {
		this.value = value;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visitText(this);
	}
}
