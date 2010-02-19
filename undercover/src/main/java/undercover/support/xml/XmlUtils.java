package undercover.support.xml;

import java.util.HashMap;
import java.util.Map;

public class XmlUtils {
	private static final Map<Character, String> HTML_ESCAPE_CHARACTERS = new HashMap<Character, String>();
	static {
		HTML_ESCAPE_CHARACTERS.put('&', "&amp;");
		HTML_ESCAPE_CHARACTERS.put('<', "&lt;");
		HTML_ESCAPE_CHARACTERS.put('>', "&gt;");
		HTML_ESCAPE_CHARACTERS.put('"', "&quot;");
	}
	
	public static String escape(String str) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			String escaped = HTML_ESCAPE_CHARACTERS.get(c);
			if (escaped == null) {
				result.append(c);
			} else {
				result.append(escaped);
			}
		}
		return result.toString();
	}
}
