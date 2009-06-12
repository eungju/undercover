package undercover.instrument.filter;

import static undercover.instrument.filter.ExclusionUtils.*;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import undercover.instrument.Scala;

public class ScalaTagExclusion implements Exclusion {
	public boolean exclude(ClassNode classNode) {
		return false;
	}
	
	public boolean exclude(ClassNode classNode, MethodNode methodNode) {
		return Scala.isClass(classNode) && matchMethod(methodNode, "$tag", "()I");
	}
}
