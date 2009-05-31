package undercover.report.html;

import org.antlr.stringtemplate.AttributeRenderer;

import undercover.report.Item;

public class ItemRenderer implements AttributeRenderer {
	public String toString(Object value) {
		return ((Item) value).getDisplayName();
	}

	public String toString(Object value, String formatName) {
		Item item = (Item) value;
		if ("linkName".equals(formatName)) {
			return item.getLinkName();
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}
}
