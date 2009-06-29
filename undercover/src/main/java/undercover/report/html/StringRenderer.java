package undercover.report.html;

import org.antlr.stringtemplate.AttributeRenderer;

import undercover.support.HtmlUtils;

public class StringRenderer implements AttributeRenderer {
	public String toString(Object value) {
		return value.toString();
	}

	public String toString(Object value, String formatName) {
		if ("html".equals(formatName)) {
			return HtmlUtils.escape((String) value);
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}
}
