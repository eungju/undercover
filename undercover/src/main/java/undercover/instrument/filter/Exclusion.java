package undercover.instrument.filter;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface Exclusion {
	boolean exclude(ClassNode classNode);
	boolean exclude(ClassNode classNode, MethodNode methodNode);
}
