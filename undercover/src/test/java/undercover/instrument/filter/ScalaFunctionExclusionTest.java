package undercover.instrument.filter;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ScalaFunctionExclusionTest {
	private ScalaFunctionExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new ScalaFunctionExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_SYNTHETIC, "net/me2day/scala/HttpInvocation$$anonfun$execute$1", null, "java/lang/Object", new String[] { "scala/Function1", "scala/ScalaObject", "java/io/Serializable" });

	}

	@Test public void excludeConstructor() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "<init>", "(Lnet/me2day/scala/HttpInvocation;Lscala/runtime/ObjectRef;)V", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void includeAppry() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_FINAL, "apply", "(Lscala/Tuple2;)V", "(Lscala/Tuple2<Ljava/lang/String;Ljava/lang/String;>;)V", null);
		assertFalse(dut.exclude(classNode, methodNode));
	}
	
	//Function0
	@Test public void excludeToString() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}

	//Function1
	@Test public void excludeCompose() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "compose", "(Lscala/Function1;)Lscala/Function1;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	//Function1
	@Test public void excludeAndThen() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "andThen", "(Lscala/Function1;)Lscala/Function1;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}

	//Function2
	@Test public void curry() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "curry", "()Lscala/Function1;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
}
