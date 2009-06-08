package undercover.instrument.filter;

import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class JavaEnumExclusionTest {
	private JavaEnumExclusion dut;
	private ClassNode classNode;
	
	@Before public void beforeEach() {
		dut = new JavaEnumExclusion();
		classNode = new ClassNode();
		classNode.visit(V1_5, ACC_ENUM, "tokyotyrant/protocol/CommandState", "Ljava/lang/Enum<Ltokyotyrant/protocol/CommandState;>;", "java/lang/Enum", null);
	}
	
	@Test public void excludeValues() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "values", "()[Ltokyotyrant/protocol/CommandState;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
	
	@Test public void excludeValueOf() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "valueOf", "(Ljava/lang/String;)Ltokyotyrant/protocol/CommandState;", null, null);
		assertTrue(dut.exclude(classNode, methodNode));
	}
}
