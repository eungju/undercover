package undercover.instrument;

import org.objectweb.asm.tree.ClassNode;

import undercover.data.ClassMeta;

public class AnonymousDetector {
	public ClassMeta.Outer inspect(ClassNode classNode) {
		if (classNode.outerClass != null) {
			String methodName = classNode.outerMethod == null ? null : classNode.outerMethod + classNode.outerMethodDesc;
			return new ClassMeta.Outer(classNode.outerClass, methodName);
		}
		return null;
	}
}
