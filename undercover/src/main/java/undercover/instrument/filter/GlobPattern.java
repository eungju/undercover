package undercover.instrument.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Ant style <a href="http://ant.apache.org/manual/dirtasks.html#patterns">glob pattern matching</a>.
 */
public class GlobPattern {
	final Pattern pattern;
	
	public GlobPattern(String expression) {
		pattern = Pattern.compile(toRegex(expression));
	}

	public boolean match(String name) {
		return pattern.matcher(name).matches();
	}
	
	String toRegex(String expression) {
		StringBuilder regex = new StringBuilder();
		int length = expression.length();
		int index = 0;
		while (index < length) {
			char c = expression.charAt(index);
			if (c == '*') {
				if (expression.startsWith("**/", index)) {
					regex.append("([^/]*/)*");
					index += 2;
				} else if (expression.substring(index).equals("**")) {
					regex.append("([^/]*/)*[^/]*");
					index += 1;
				} else {
					regex.append("[^/]*");
				}
			} else if (c == '?') {
				regex.append("[^/]");
			} else if ("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".indexOf(c) >= 0) {
				regex.append("\\").append(c);
			} else {
				regex.append(c);
			}
			index++;
		}
		return regex.toString();
	}

	public static Collection<GlobPattern> compile(Collection<String> expression) {
		Collection<GlobPattern> result = new ArrayList<GlobPattern>();
		for (String each : expression) {
			result.add(new GlobPattern(each));
		}
		return result;
	}
}
