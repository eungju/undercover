package undercover.instrument.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ExclusionSet implements Exclusion {
	private final Collection<Exclusion> exclusions;
	
	public ExclusionSet() {
		exclusions = new ArrayList<Exclusion>();
	}

	public ExclusionSet(Collection<Exclusion> exclusions) {
		this();
		this.exclusions.addAll(exclusions);
	}
	
	public void add(Exclusion exclusion) {
		exclusions.add(exclusion);
	}

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
	
	public static ExclusionSet withDefault() {
		return new ExclusionSet(Arrays.asList(new BridgeMethodExclusion(), new NestedClassExclusion(), new EnumExclusion()));
	}
}
