package undercover.instrument.synthetic;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class NoExclusion implements Exclusion {
	public boolean exclude(ClassNode classNode) {
		return false;
	}

	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return false;
	}
}
