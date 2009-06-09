package undercover.instrument;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;

import undercover.data.ClassMeta;

public class AnonymousDetectorTest {
	private AnonymousDetector dut;

	public static class NestedClass {
		public static class NestedNestedClass {
		}
	}
	
	public Object anonymous = new Object() {
		public Object anonymous = new Object() {
		};
	};
	
	@Before public void beforeEach() {
		dut = new AnonymousDetector();
	}
	
	@Test public void namedNestedClass() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "undercover/instrument/AnonymousDetectorTest$NestedClass", null, "java/lang/Object", null);
		cw.visitSource("AnonymousDetectorTest.java", null);
		cw.visitInnerClass("undercover/instrument/AnonymousDetectorTest$NestedClass", "undercover/instrument/AnonymousDetectorTest", "NestedClass", ACC_PUBLIC + ACC_STATIC);
		cw.visitInnerClass("undercover/instrument/AnonymousDetectorTest$NestedClass$NestedNestedClass", "undercover/instrument/AnonymousDetectorTest$NestedClass", "NestedNestedClass", ACC_PUBLIC + ACC_STATIC);
		ClassMeta.Outer outer = dut.inspect(cw);
		assertNull(outer);
	}
	
	@Test public void anonymousNestedClassEnclosedByClass() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_SUPER, "schemaless/gardener/IndexGardenerTest$1", null, "org/jmock/integration/junit4/JUnit4Mockery", null);
		cw.visitSource("IndexGardenerTest.java", null);
		cw.visitOuterClass("schemaless/gardener/IndexGardenerTest", null, null);
		cw.visitInnerClass("schemaless/gardener/IndexGardenerTest$1", null, null, 0);
		ClassMeta.Outer outer = dut.inspect(cw);
		assertEquals(cw.outerClass, outer.className);
		assertFalse(outer.isMethod());
	}
	
	@Test public void anonymousNestedClassEnclosedByMethod() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_SUPER, "schemaless/gardener/IndexGardenerTest$2", null, "org/jmock/Expectations", null);
		cw.visitSource("IndexGardenerTest.java", null);
		cw.visitOuterClass("schemaless/gardener/IndexGardenerTest", "inspectAll", "()V");
		cw.visitInnerClass("schemaless/gardener/IndexGardenerTest$2", null, null, 0);
		ClassMeta.Outer outer = dut.inspect(cw);
		assertEquals(cw.outerClass, outer.className);
		assertTrue(outer.isMethod());
		assertEquals(cw.outerMethod + cw.outerMethodDesc, outer.methodName);
	}
	
	@Test public void scalaFunctionLiteral() {
		ClassNode cw = new ClassNode();
		cw.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER + ACC_SYNTHETIC, "net/me2day/scala/XmlResultParser$$anonfun$tags$1", null, "java/lang/Object", new String[] { "scala/Function1", "scala/ScalaObject" });
		cw.visitSource("XmlResultParser.scala", null);
		// ATTRIBUTE Scala
		cw.visitInnerClass("net/me2day/scala/XmlResultParser$$anonfun$tags$1", "net/me2day/scala/XmlResultParser", "$anonfun$tags$1", ACC_PUBLIC + ACC_FINAL + ACC_SYNCHRONIZED + ACC_SYNTHETIC);
		ClassMeta.Outer outer = dut.inspect(cw);
		assertEquals("net/me2day/scala/XmlResultParser", outer.className);
		assertFalse(outer.isMethod());
	}
}
