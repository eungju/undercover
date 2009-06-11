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
			if (Scala.isFunctionClass(classNode)) {
				for (InnerClassNode each : (List<InnerClassNode>) classNode.innerClasses) {
					if (each.name.equals(classNode.name) && each.outerName != null) {
						return new ClassMeta.Outer(each.outerName, null);
					}
				}
			}
		}
		return null;
	}
}
