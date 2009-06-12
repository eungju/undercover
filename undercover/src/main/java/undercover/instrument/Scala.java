package undercover.instrument;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class Scala {
	static boolean containsMatching(List<String> interfaces, String regex) {
		for (String each : interfaces) {
			if (each.matches(regex)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isClass(ClassNode classNode) {
		return classNode.interfaces.contains("scala/ScalaObject");
	}
	
	public static boolean isFunctionClass(ClassNode classNode) {
		return isClass(classNode) && containsMatching(classNode.interfaces, "scala/Function\\d+");
	}

	public static boolean isCaseClass(ClassNode classNode) {
		return isClass(classNode) && classNode.interfaces.contains("scala/Product");
	}
}
