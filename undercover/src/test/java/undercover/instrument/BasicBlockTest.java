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
	
	@Test public void tryCatchBlockHandlersAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "tryCatchBranch", "()V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/RuntimeException");
		//0
		mv.visitLabel(l0);
		mv.visitLineNumber(62, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		mv.visitInsn(POP);
		mv.visitLabel(l1);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		//7
		mv.visitLabel(l2);
		mv.visitLineNumber(63, l2);
		mv.visitVarInsn(ASTORE, 1);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(64, l4);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b2", "()Z");
		mv.visitInsn(POP);
		mv.visitLabel(l3);
		//16
		mv.visitLineNumber(66, l3);
		mv.visitInsn(RETURN);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l5, 0);
		mv.visitLocalVariable("e", "Ljava/lang/RuntimeException;", null, l4, l3, 1);
		mv.visitMaxs(1, 2);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(Arrays.asList(new BasicBlock(0, 6), new BasicBlock(7, 15), new BasicBlock(16, 17)), dut.blocks);
	}
	
	@Test public void backwardJumpTargetsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "forLoop", "(I)V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		//0
		mv.visitLabel(l0);
		mv.visitLineNumber(101, l0);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 2);
		Label l1 = new Label();
		mv.visitLabel(l1);
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		//6
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(102, l3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		mv.visitInsn(POP);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(101, l4);
		mv.visitIincInsn(2, 1);
		mv.visitLabel(l2);
		//15
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitJumpInsn(IF_ICMPLT, l3);
		//18
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLineNumber(104, l5);
		mv.visitInsn(RETURN);
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l6, 0);
		mv.visitLocalVariable("end", "I", null, l0, l6, 1);
		mv.visitLocalVariable("i", "I", null, l1, l5, 2);
		mv.visitMaxs(2, 3);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(Arrays.asList(new BasicBlock(0, 5), new BasicBlock(6, 14), new BasicBlock(15, 17), new BasicBlock(18, 20)), dut.blocks);
	}
}
