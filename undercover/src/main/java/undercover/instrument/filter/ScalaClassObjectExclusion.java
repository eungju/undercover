package undercover.instrument.filter;

import static org.objectweb.asm.Opcodes.*;
import static undercover.instrument.filter.ExclusionUtils.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Exclude synthetic classes for compiler generated class object classes.
 */
public class ScalaClassObjectExclusion implements Exclusion {
	public boolean exclude(ClassNode classNode) {
		return
			hasAccess(classNode.access, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_SYNTHETIC) &&
			classNode.name.endsWith("$") &&
			classNode.interfaces.contains("scala/ScalaObject");
	}

	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return false;
	}
}
