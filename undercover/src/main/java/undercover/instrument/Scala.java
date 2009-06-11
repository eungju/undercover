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
	
	public static boolean isFunctionClass(ClassNode classNode) {
		return classNode.interfaces.contains("scala/ScalaObject") && containsMatching(classNode.interfaces, "scala/Function\\d");
	}
}
