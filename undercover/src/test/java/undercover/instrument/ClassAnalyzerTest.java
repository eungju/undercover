package undercover.instrument;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import undercover.data.ClassMeta;
import undercover.instrument.filter.NoExclusion;

public class ClassAnalyzerTest {
	private ClassAnalyzer dut;

	@Before public void beforeEach() {
		dut = new ClassAnalyzer(new NoExclusion());
	}
	
	@Test public void nestedClass() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_SUPER, "undercover/instrument/ClassAnalyzerTest$NestedClass", null, "java/lang/Object", null);
		cw.visitSource("ClassAnalyzerTest.java", null);
		cw.visitInnerClass("undercover/instrument/ClassAnalyzerTest$NestedClass", "undercover/instrument/ClassAnalyzerTest", "NestedClass", 0);
		ClassMeta classMeta = dut.instrument(cw);
		assertFalse(classMeta.isAnonymous());
	}
	
	@Test public void anonymousNestedClassEnclosedByClass() {
		ClassNode classNode = new ClassNode();
		classNode.visit(V1_5, ACC_SUPER, "schemaless/gardener/IndexGardenerTest$1", null, "org/jmock/integration/junit4/JUnit4Mockery", null);
		classNode.visitSource("IndexGardenerTest.java", null);
		classNode.visitOuterClass("schemaless/gardener/IndexGardenerTest", null, null);
		classNode.visitInnerClass("schemaless/gardener/IndexGardenerTest$1", null, null, 0);
		ClassMeta classMeta = dut.instrument(classNode);
		assertTrue(classMeta.isAnonymous());
		assertFalse(classMeta.outer.isMethod());
		assertEquals(classNode.outerClass, classMeta.outer.className);
	}
	
	@Test public void anonymousNestedClassEnclosedByMethod() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_SUPER, "schemaless/gardener/IndexGardenerTest$2", null, "org/jmock/Expectations", null);
		cw.visitSource("IndexGardenerTest.java", null);
		cw.visitOuterClass("schemaless/gardener/IndexGardenerTest", "inspectAll", "()V");
		cw.visitInnerClass("schemaless/gardener/IndexGardenerTest$2", null, null, 0);
		ClassMeta classMeta = dut.instrument(cw);
		assertTrue(classMeta.isAnonymous());
		assertTrue(classMeta.outer.isMethod());
		assertEquals(cw.outerClass, classMeta.outer.className);
		assertEquals(cw.outerMethod + cw.outerMethodDesc, classMeta.outer.methodName);
	}
}
