package undercover.instrument.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GlobFilter {
	private Collection<GlobPattern> includePatterns;
	private Collection<GlobPattern> excludePatterns;
	
	public GlobFilter() {
		includePatterns = new ArrayList<GlobPattern>();
		excludePatterns = new ArrayList<GlobPattern>();
	}
	
	public GlobFilter(Collection<String> includes, Collection<String> excludes) {
		includePatterns = GlobPattern.compile(includes);
		excludePatterns = GlobPattern.compile(excludes);
	}

	public GlobFilter(String[] includes, String[] excludes) {
		includePatterns = GlobPattern.compile(Arrays.asList(includes));
		excludePatterns = GlobPattern.compile(Arrays.asList(excludes));
	}

	public void addInclude(GlobPattern pattern) {
		includePatterns.add(pattern);
	}
	
	public void addExclude(GlobPattern pattern) {
		excludePatterns.add(pattern);
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
}
