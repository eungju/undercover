package undercover.instrument.filter;

import static undercover.instrument.filter.ExclusionUtils.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import undercover.instrument.Scala;

public class ScalaFunctionExclusion extends NoExclusion {
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return Scala.isFunctionClass(classNode) && (
				methodNode.name.equals("<init>") ||
				isToStringMethod(methodNode) ||
				isComposeMethod(methodNode) ||
				isAndThenMethod(methodNode) ||
				isCurryMethod(methodNode));
	}

	boolean isComposeMethod(MethodNode methodNode) {
		return matchMethod(methodNode, "compose", "(Lscala/Function1;)Lscala/Function1;");
	}

	boolean isAndThenMethod(MethodNode methodNode) {
		return matchMethod(methodNode, "andThen", "(Lscala/Function1;)Lscala/Function1;");
	}

	boolean isCurryMethod(MethodNode methodNode) {
		return matchMethod(methodNode, "curry", "()Lscala/Function1;");
	}
}
