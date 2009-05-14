package undercover.report.html;

import org.antlr.stringtemplate.AttributeRenderer;

public class DoubleRenderer implements AttributeRenderer {
	public String toString(Object value) {
		return ((Double) value).toString();
	}

	public String toString(Object value, String formatName) {
		if ("percent".equals(formatName)) {
			return String.format("%.1f%%", ((Double) value) * 100);
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}
}
