package undercover.report.html;

import org.antlr.stringtemplate.AttributeRenderer;

public class DoubleRenderer implements AttributeRenderer {
	String trimZeros(String str) {
		return str.replaceFirst("\\.?0+$", "");
	}
	
	public String toString(Object value) {
		return trimZeros(String.format("%.2f", (Double) value));
	}

	public String toString(Object value, String formatName) {
		if ("percent".equals(formatName)) {
			return trimZeros(String.format("%.1f", ((Double) value) * 100)) + "%";
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}
}
