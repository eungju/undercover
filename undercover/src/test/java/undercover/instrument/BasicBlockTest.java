package undercover.instrument;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class BasicBlockTest implements Opcodes {
	private BasicBlockAnalyzer dut;
	
	@Before public void beforeEach() {
		dut = new BasicBlockAnalyzer();
	}
	
	@Test public void returnInstructionsAreEdges() {
		MethodNode methodNode = new MethodNode(ACC_PUBLIC, "empty", "()V", null, null);
		methodNode.visitCode();
		Label l0 = new Label();
		methodNode.visitLabel(l0);
		methodNode.visitLineNumber(11, l0);
		methodNode.visitInsn(RETURN);
		Label l1 = new Label();
		methodNode.visitLabel(l1);
		methodNode.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l1, 0);
		methodNode.visitMaxs(0, 1);
		methodNode.visitEnd();
		
		dut.analyze(methodNode);
		assertEquals(Arrays.asList(new BasicBlock(0, 2)), dut.blocks);
	}
	
	@Test public void jumpInstructionsAndTargetLabelsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "ifBranch", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		//0
		mv.visitLabel(l0);
		mv.visitLineNumber(43, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		//5
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(44, l2);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b2", "()Z");
		mv.visitInsn(POP);
		mv.visitLabel(l1);
		//11
		mv.visitLineNumber(46, l1);
		mv.visitInsn(RETURN);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l3, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(Arrays.asList(new BasicBlock(0, 4), new BasicBlock(5, 10), new BasicBlock(11, 12)), dut.blocks);
	}
}
