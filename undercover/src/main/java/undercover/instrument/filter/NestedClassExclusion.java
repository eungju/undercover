package undercover.instrument.filter;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class NestedClassExclusion extends NoExclusion {
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return ExclusionUtils.hasAccess(methodNode.access, ACC_STATIC | ACC_SYNTHETIC) &&
			methodNode.name.startsWith("access$") && methodNode.desc.startsWith("(L" + classNode.name);
	}
}
