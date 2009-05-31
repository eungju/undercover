package undercover.instrument.synthetic;

import static org.objectweb.asm.Opcodes.*;

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
		final int ACCESS = ACC_FINAL + ACC_SUPER + ACC_ENUM;
		return
			((classNode.access & ACCESS) == ACCESS) &&
			classNode.superName.equals("java/lang/Enum");
	}
	
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		if (isEnumClass(classNode)) {
			if (methodNode.name.equals("values") && methodNode.desc.equals("()[L" + classNode.name + ";")) {
				return true;
			}
			else if (methodNode.name.equals("valueOf") && methodNode.desc.equals("(Ljava/lang/String;)L" + classNode.name + ";")) {
				return true;
			}
		}
		return false;
	}
}
