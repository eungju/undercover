package undercover.instrument.synthetic;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class BridgeMethodExclusionTest {
	private BridgeMethodExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new BridgeMethodExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_5, ACC_SUPER, "com/nhn/dashboard/client/inquiry/OrganizationTreePresenter$1", "Ljava/lang/Object;Lcom/google/gwt/user/client/rpc/AsyncCallback<Ljava/util/List<Lcom/nhn/dashboard/client/inquiry/Department;>;>;", "java/lang/Object", new String[] { "com/google/gwt/user/client/rpc/AsyncCallback" });
	}

	@Test public void exclude() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "onSuccess", "(Ljava/lang/Object;)V", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void doNotExclude() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "onSuccess", "(Ljava/util/List;)V", "(Ljava/util/List<Lcom/nhn/dashboard/client/inquiry/Department;>;)V", null);
		assertFalse(dut.exclude(classNode, methodNode));
	}
}
