package undercover.report.html;

import org.antlr.stringtemplate.AttributeRenderer;
import org.apache.commons.lang.StringEscapeUtils;

public class StringRenderer implements AttributeRenderer {
	public String toString(Object o) {
		return o.toString();
	}

	public String toString(Object o, String formatName) {
		if ("html".equals(formatName)) {
			return StringEscapeUtils.escapeHtml((String) o);
		}
		throw new IllegalArgumentException("Unsupported format name " + formatName);
	}
}
