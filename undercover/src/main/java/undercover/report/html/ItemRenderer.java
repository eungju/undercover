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
		} else if ("coveragePercent".equals(formatName)) {
			return coveragePercent(item);
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}

	String coveragePercent(Item item) {
		if (item.isExecutable()) {
			return String.format("%.1f", ((Double) item.getCoverageRate()) * 100) + "%";
		} else {
			return "N/A";
		}
	}
}
