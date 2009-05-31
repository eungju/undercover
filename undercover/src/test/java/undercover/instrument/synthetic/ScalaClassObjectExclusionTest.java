package undercover.instrument.synthetic;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

public class ScalaClassObjectExclusionTest {
	private ScalaClassObjectExclusion dut;
	
	@Before public void beforeEach() {
		dut = new ScalaClassObjectExclusion();
	}
	
	@Test public void excludeCompilerGenerated() {
		ClassNode classNode = new ClassNode();
		classNode.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_SYNTHETIC, "net/me2day/scala/Comment$", null, "java/lang/Object", new String[] { "scala/Function4", "scala/ScalaObject" });
		assertTrue(dut.exclude(classNode));
	}
	
	@Test public void includeHandwritten() {
		ClassNode classNode = new ClassNode();
		classNode.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, "net/me2day/scala/Me2Day$", null, "java/lang/Object", new String[] { "scala/ScalaObject" });
		assertFalse(dut.exclude(classNode));
	}
}
