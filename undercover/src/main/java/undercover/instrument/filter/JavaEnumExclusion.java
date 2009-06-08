package undercover.instrument.filter;

import static org.objectweb.asm.Opcodes.*;
import static undercover.instrument.filter.ExclusionUtils.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Exclude synthetic methods for enums.
 */
public class JavaEnumExclusion implements Exclusion {
	public boolean exclude(ClassNode classNode) {
		return false;
	}

	boolean isEnumClass(ClassNode classNode) {
		return
			hasAccess(classNode.access, ACC_ENUM) &&
			classNode.superName.equals("java/lang/Enum");
	}
	
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		if (isEnumClass(classNode)) {
			if (hasAccess(methodNode.access, ACC_PUBLIC + ACC_STATIC) &&
					methodNode.name.equals("values") &&
					methodNode.desc.equals("()[L" + classNode.name + ";")) {
				return true;
			} else if (hasAccess(methodNode.access, ACC_PUBLIC + ACC_STATIC) &&
					methodNode.name.equals("valueOf") && methodNode.desc.equals("(Ljava/lang/String;)L" + classNode.name + ";")) {
				return true;
			}
		}
		return false;
	}
}
