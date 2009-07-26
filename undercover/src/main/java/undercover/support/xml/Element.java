package undercover.support.xml;

import java.util.ArrayList;
import java.util.List;

public class Element implements Node {
	public final String name;
	public final List<NameValuePair> attributes;
	public final List<Node> children;
	
	public Element(String name) {
		this.name = name;
		this.attributes = new ArrayList<NameValuePair>();
		this.children = new ArrayList<Node>();
	}
	
	public Element attr(String name, String value) {
		attributes.add(new NameValuePair(name, value));
		return this;
	}

	public Element attr(String name, int value) {
		return attr(name, String.valueOf(value));
	}

	public Element attr(String name, long value) {
		return attr(name, String.valueOf(value));
	}

	public Element attr(String name, double value) {
		return attr(name, String.valueOf(value));
	}

	public String attr(String name) {
		for (NameValuePair each : attributes) {
			if (each.name.equals(name)) {
				return each.value;
			}
		}
		return null;
	}
	
	public Element append(Node... nodes) {
		for (Node each : nodes) {
			children.add(each);
		}
		return this;
	}
	
	public void accept(NodeVisitor visitor) {
		visitor.enterElement(this);
		for (Node each : children) {
			each.accept(visitor);
		}		
		visitor.leaveElement(this);
	}
}
