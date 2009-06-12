package undercover.instrument.filter;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ScalaCaseClassExclusionTest {
	private ScalaCaseClassExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new ScalaCaseClassExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "net/me2day/scala/Tag", null, "java/lang/Object", new String[] { "scala/ScalaObject", "scala/Product", "java/io/Serializable" });
	}

	@Test public void excludeProductElement() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeProductArity() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "productArity", "()I", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeProductPrefix() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "productPrefix", "()Ljava/lang/String;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeEquals() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);;
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeHashCode() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "hashCode", "()I", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeToString() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeUnknownThing() {
		MethodNode methodNode = new MethodNode(ACC_PRIVATE + ACC_FINAL + ACC_SYNTHETIC, "gd2$1", "(Ljava/net/URL;Ljava/lang/String;)Z", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
}
