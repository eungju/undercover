package undercover.instrument.synthetic;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Exclude synthetic classes for compiler generated class object classes.
 */
public class ScalaClassObjectExclusion implements Exclusion {
	public boolean exclude(ClassNode classNode) {
		final int ACCESS = ACC_FINAL + ACC_SUPER + ACC_SYNTHETIC;
		return
			((classNode.access & ACCESS) == ACCESS) &&
			classNode.name.endsWith("$") &&
			classNode.interfaces.contains("scala/ScalaObject");
	}

	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return false;
	}
}
