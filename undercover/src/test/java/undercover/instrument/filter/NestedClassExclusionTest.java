package undercover.instrument.filter;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class NestedClassExclusionTest {
	private NestedClassExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new NestedClassExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "com/nhn/dashboard/client/inquiry/SearchFormPresenter", null, "java/lang/Object", null);
	}
	
	@Test public void excludeOuterPrivateAccessor() {
		MethodNode methodNode = new MethodNode(ACC_STATIC + ACC_SYNTHETIC, "access$0", "(Lcom/nhn/dashboard/client/inquiry/SearchFormPresenter;Ljava/util/List;)V", null, null);;
		assertTrue(dut.exclude(classNode, methodNode));
	}
}
