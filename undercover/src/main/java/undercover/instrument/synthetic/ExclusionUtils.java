package undercover.instrument.synthetic;

public class ExclusionUtils {
	public static boolean hasAccess(int access, int required) {
		return (access & required) == required;
	}
}
