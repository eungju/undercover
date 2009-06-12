package undercover.instrument.filter;

import org.objectweb.asm.tree.MethodNode;

public class ExclusionUtils {
	public static boolean hasAccess(int access, int required) {
		return (access & required) == required;
	}
	
	public static boolean matchMethod(MethodNode methodNode, String name, String desc) {
		return (methodNode.name.equals(name) && methodNode.desc.equals(desc));
	}
	
	public static boolean isToStringMethod(MethodNode methodNode) {
		return matchMethod(methodNode, "toString", "()Ljava/lang/String;");
	}
	
	public static boolean isEquals(MethodNode methodNode) {
		return matchMethod(methodNode, "equals", "(Ljava/lang/Object;)Z");
	}
	
	public static boolean isHashCode(MethodNode methodNode) {
		return matchMethod(methodNode, "hashCode", "()I");
	}
}
