package undercover.instrument.filter;

import static org.objectweb.asm.Opcodes.*;
import static undercover.instrument.filter.ExclusionUtils.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import undercover.instrument.Scala;

public class ScalaCaseClassExclusion extends NoExclusion {
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return Scala.isCaseClass(classNode) && (
				matchMethod(methodNode, "productElement", "(I)Ljava/lang/Object;") ||
				matchMethod(methodNode, "productArity", "()I") ||
				matchMethod(methodNode, "productPrefix", "()Ljava/lang/String;") ||
				ExclusionUtils.isEquals(methodNode) ||
				ExclusionUtils.isHashCode(methodNode) ||
				ExclusionUtils.isToStringMethod(methodNode) ||
				(hasAccess(methodNode.access, ACC_PRIVATE + ACC_FINAL + ACC_SYNTHETIC) && methodNode.name.matches("gd\\d+\\$\\d+")));
	}
}
