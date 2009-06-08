package undercover.instrument.filter;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ScalaTagExclusionTest {
	private ScalaTagExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new ScalaTagExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "net/me2day/scala/Me2Day", null, "java/lang/Object", new String[] { "scala/ScalaObject" });
	}

	@Test public void exclude() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "$tag", "()I", null, new String[] { "java/rmi/RemoteException" });
		assertTrue(dut.exclude(classNode, methodNode));
	}
}
