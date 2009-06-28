package undercover.instrument.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Folows <a href="http://emma.sourceforge.net/reference/ch02s06s02.html">Emma inclusion/exclusion matching algorithm</a>.
 */
public class GlobFilter {
	private final Collection<GlobPattern> includePatterns;
	private final Collection<GlobPattern> excludePatterns;
	
	public GlobFilter(String[] includes, String[] excludes) {
		includePatterns = patterns(Arrays.asList(includes));
		excludePatterns = patterns(Arrays.asList(excludes));
	}
	
	public boolean accept(String name) {
		if (!includePatterns.isEmpty()) {
			boolean included = false;
			for (GlobPattern each : includePatterns) {
				if (each.match(name)) {
					included = true;
					break;
				}
			}
			if (!included) {
				return false;
			}
		}
		if (!excludePatterns.isEmpty()) {
			for (GlobPattern each : excludePatterns) {
				if (each.match(name)) {
					return false;
				}
			}
		}
		return true;
	}

	static Collection<GlobPattern> patterns(Collection<String> expression) {
		Collection<GlobPattern> result = new ArrayList<GlobPattern>();
		for (String each : expression) {
			result.add(new GlobPattern(each));
		}
		return result;
	}
}
