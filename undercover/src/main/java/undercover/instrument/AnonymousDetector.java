package undercover.instrument;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;

import undercover.data.ClassMeta;

public class AnonymousDetector {
	public ClassMeta.Outer inspect(ClassNode classNode) {
		if (classNode.outerClass != null) {
			String methodName = classNode.outerMethod == null ? null : classNode.outerMethod + classNode.outerMethodDesc;
			return new ClassMeta.Outer(classNode.outerClass, methodName);
		} else if (classNode.innerClasses.size() > 0) {
			if (isScalaFunctionClass(classNode)) {
				for (InnerClassNode each : (List<InnerClassNode>) classNode.innerClasses) {
					if (each.name.equals(classNode.name) && each.outerName != null) {
						return new ClassMeta.Outer(each.outerName, null);
					}
				}
			}
		}
		return null;
	}

	boolean containsMatching(List<String> interfaces, String regex) {
		for (String each : interfaces) {
			if (each.matches(regex)) {
				return true;
			}
		}
		return false;
	}

	boolean isScalaFunctionClass(ClassNode classNode) {
		return classNode.interfaces.contains("scala/ScalaObject") && containsMatching(classNode.interfaces, "scala/Function\\d");
	}
}
