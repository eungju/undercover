package undercover.instrument.filter;

import org.objectweb.asm.tree.ClassNode;

public class GlobExclusion extends NoExclusion {
	private final GlobFilter globFilter;
	
	public GlobExclusion(GlobFilter globFilter) {
		this.globFilter = globFilter;
	}
	
	public boolean exclude(ClassNode classNode) {
		return !globFilter.accept(classNode.name);
	}
}
