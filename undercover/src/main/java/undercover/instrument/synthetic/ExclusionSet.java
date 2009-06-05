package undercover.instrument.synthetic;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ExclusionSet implements Exclusion {
	private final Exclusion[] exclusions = new Exclusion[] {
			new BridgeMethodExclusion(),
			new JavaEnumExclusion(),
			new ScalaClassObjectExclusion(),
			new ScalaTagExclusion(),
	};

	public boolean exclude(ClassNode classNode) {
		for (Exclusion each : exclusions) {
			if (each.exclude(classNode)) {
				return true;
			}
		}
		return false;
	}

	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		for (Exclusion each : exclusions) {
			if (each.exclude(classNode, methodNode)) {
				return true;
			}
		}
		return false;
	}
}
