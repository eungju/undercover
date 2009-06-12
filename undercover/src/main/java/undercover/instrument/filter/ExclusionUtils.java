package undercover.instrument.filter;

public class ExclusionUtils {
	public static boolean hasAccess(int access, int required) {
		return (access & required) == required;
	}
}
