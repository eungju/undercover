package undercover.instrument;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	
	<T> Set<T> set(T... elements) {
		Set<T> result = new HashSet<T>(elements.length);
		for (T each : elements) {
			result.add(each);
		}
		return result;
	}
	
	@Test public void abstractMethodsHaveNoEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC + ACC_ABSTRACT, "abstractMethod", "()V", null, null);
		mv.visitEnd();
		
		dut.analyze(mv);
		assertEquals(Arrays.<BasicBlock>asList(), dut.blocks);
		assertEquals(1, dut.complexity);
	}
	
	@Test public void returnInstructionsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "empty", "()V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(11, l0);
		mv.visitInsn(RETURN);
		//1
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l1, 0);
		mv.visitMaxs(0, 1);
		mv.visitEnd();
		
		dut.analyze(mv);
		assertEquals(Arrays.asList(new BasicBlock(0, 1, set(11))), dut.blocks);
		assertEquals(1, dut.complexity);
	}
	
	@Test public void throwInstructionsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "throwingMethod", "()V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(97, l0);
		mv.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "()V");
		mv.visitInsn(ATHROW);
		//4
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l1, 0);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
		
		dut.analyze(mv);
		assertEquals(Arrays.asList(new BasicBlock(0, 4, set(97))), dut.blocks);
		assertEquals(1, dut.complexity);
	}
	
	@Test public void jumpInstructionsAndTargetLabelsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "ifBranch", "()V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(43, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		//3
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(44, l2);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b2", "()Z");
		mv.visitInsn(POP);
		//6
		mv.visitLabel(l1);
		mv.visitLineNumber(46, l1);
		mv.visitInsn(RETURN);
		//7
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l3, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 3, set(43)),
						new BasicBlock(3, 6, set(44)),
						new BasicBlock(6, 7, set(46))),
				dut.blocks);
		assertEquals(2, dut.complexity);
	}
	
	@Test public void tryCatchBlockHandlersAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "tryCatchBranch", "()V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/RuntimeException");
		mv.visitLabel(l0);
		mv.visitLineNumber(62, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		mv.visitInsn(POP);
		mv.visitLabel(l1);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		//4
		mv.visitLabel(l2);
		mv.visitLineNumber(63, l2);
		mv.visitVarInsn(ASTORE, 1);
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(64, l4);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b2", "()Z");
		mv.visitInsn(POP);
		//8
		mv.visitLabel(l3);
		mv.visitLineNumber(66, l3);
		mv.visitInsn(RETURN);
		//9
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l5, 0);
		mv.visitLocalVariable("e", "Ljava/lang/RuntimeException;", null, l4, l3, 1);
		mv.visitMaxs(1, 2);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 4, set(62)),
						new BasicBlock(4, 8, set(63, 64)),
						new BasicBlock(8, 9, set(66))),
				dut.blocks);
		assertEquals(1, dut.complexity);
	}
	
	@Test public void backwardJumpTargetsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "forLoop", "(I)V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(101, l0);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 2);
		Label l1 = new Label();
		mv.visitLabel(l1);
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		//3
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
		//7
		mv.visitLabel(l2);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitJumpInsn(IF_ICMPLT, l3);
		//10
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLineNumber(104, l5);
		mv.visitInsn(RETURN);
		//11
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l6, 0);
		mv.visitLocalVariable("end", "I", null, l0, l6, 1);
		mv.visitLocalVariable("i", "I", null, l1, l5, 2);
		mv.visitMaxs(2, 3);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 3, set(101)),
						new BasicBlock(3, 7, set(102, 101)),
						new BasicBlock(7, 10, set(101)),
						new BasicBlock(10, 11, set(104))),
				dut.blocks);
		assertEquals(2, dut.complexity);
	}
	
	@Test public void tableSwitchTargetsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "tableSwitchBranches", "(I)V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(107, l0);
		mv.visitVarInsn(ILOAD, 1);
		Label l1 = new Label();
		Label l2 = new Label();
		Label l3 = new Label();
		mv.visitTableSwitchInsn(1, 2, l3, new Label[] { l1, l2 });
		//2
		mv.visitLabel(l1);
		mv.visitLineNumber(109, l1);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("1");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(110, l4);
		mv.visitJumpInsn(GOTO, l3);
		//6
		mv.visitLabel(l2);
		mv.visitLineNumber(112, l2);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("2");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		//9
		mv.visitLabel(l3);
		mv.visitLineNumber(114, l3);
		mv.visitInsn(RETURN);
		//10
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l5, 0);
		mv.visitLocalVariable("i", "I", null, l0, l5, 1);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
		
		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 2, set(107)),
						new BasicBlock(2, 6, set(109, 110)),
						new BasicBlock(6, 9, set(112)),
						new BasicBlock(9, 10, set(114))),
				dut.blocks);
		assertEquals(3, dut.complexity);
	}
	
	@Test public void lookupSwitchTargetsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "lookupSwitchBranches", "(I)V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(117, l0);
		mv.visitVarInsn(ILOAD, 1);
		Label l1 = new Label();
		Label l2 = new Label();
		Label l3 = new Label();
		mv.visitLookupSwitchInsn(l3, new int[] { 1, 2147483647 }, new Label[] { l1, l2 });
		//2
		mv.visitLabel(l1);
		mv.visitLineNumber(119, l1);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("1");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(120, l4);
		mv.visitJumpInsn(GOTO, l3);
		//6
		mv.visitLabel(l2);
		mv.visitLineNumber(122, l2);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("max");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		//9
		mv.visitLabel(l3);
		mv.visitLineNumber(124, l3);
		mv.visitInsn(RETURN);
		//10
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l5, 0);
		mv.visitLocalVariable("i", "I", null, l0, l5, 1);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
		
		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 2, set(117)),
						new BasicBlock(2, 6, set(119, 120)),
						new BasicBlock(6, 9, set(122)),
						new BasicBlock(9, 10, set(124))),
				dut.blocks);
		assertEquals(3, dut.complexity);
	}
	
	@Test public void retInstructionsAreEdges() {
		MethodNode mv = new MethodNode(ACC_PUBLIC, "tryCatchFinallyBranch", "()V", null, null);
		mv.visitCode();
		//0
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/RuntimeException");
		Label l3 = new Label();
		Label l4 = new Label();
		mv.visitTryCatchBlock(l0, l3, l4, null);
		Label l5 = new Label();
		mv.visitTryCatchBlock(l2, l5, l4, null);
		Label l6 = new Label();
		mv.visitTryCatchBlock(l4, l6, l4, null);
		mv.visitLabel(l0);
		mv.visitLineNumber(78, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b1", "()Z");
		mv.visitInsn(POP);
		mv.visitLabel(l1);
		mv.visitLineNumber(79, l1);
		Label l7 = new Label();
		mv.visitJumpInsn(JSR, l7);
		//4
		mv.visitLabel(l3);
		mv.visitLineNumber(83, l3);
		Label l8 = new Label();
		mv.visitJumpInsn(GOTO, l8);
		//5
		mv.visitLabel(l2);
		mv.visitLineNumber(79, l2);
		mv.visitVarInsn(ASTORE, 1);
		Label l9 = new Label();
		mv.visitLabel(l9);
		mv.visitLineNumber(80, l9);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b2", "()Z");
		mv.visitInsn(POP);
		Label l10 = new Label();
		mv.visitLabel(l10);
		mv.visitLineNumber(81, l10);
		mv.visitJumpInsn(JSR, l7);
		//10
		mv.visitLabel(l5);
		mv.visitLineNumber(83, l5);
		mv.visitJumpInsn(GOTO, l8);
		//11
		mv.visitLabel(l4);
		mv.visitLineNumber(82, l4);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitJumpInsn(JSR, l7);
		//13
		mv.visitLabel(l6);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitInsn(ATHROW);
		//15
		mv.visitLabel(l7);
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/instrument/HelloWorld", "b3", "()Z");
		mv.visitInsn(POP);
		mv.visitVarInsn(RET, 3);
		//20
		mv.visitLabel(l8);
		mv.visitLineNumber(84, l8);
		mv.visitInsn(RETURN);
		//21
		Label l11 = new Label();
		mv.visitLabel(l11);
		mv.visitLocalVariable("e", "Ljava/lang/RuntimeException;", null, l9, l10, 1);
		mv.visitLocalVariable("this", "Lundercover/instrument/HelloWorld;", null, l0, l11, 0);
		mv.visitMaxs(1, 4);
		mv.visitEnd();

		dut.analyze(mv);
		assertEquals(
				Arrays.asList(new BasicBlock(0, 4, set(78, 79)),
						new BasicBlock(4, 5, set(83)),
						new BasicBlock(5, 10, set(79, 80, 81)),
						new BasicBlock(10, 11, set(83)),
						new BasicBlock(11, 13, set(82)),
						new BasicBlock(13, 15, set(82)),
						new BasicBlock(15, 20, set(82)),
						new BasicBlock(20, 21, set(84))),
				dut.blocks);
		assertEquals(1, dut.complexity);
	}
}
