package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.BlockMeta;
import undercover.metric.MethodMeta;

public class InstrumentMethodVisitor implements MethodVisitor {
    protected MethodVisitor mv;

    private MethodMeta methodMeta;
	private BlockMeta blockMeta;

	private int offset;
	private Set<Integer> blockEnds;
	private Set<Label> jumpTargetLabels;
	private Set<Label> conditionalLabels;

	private Set<Integer> blockLines;
	private int lineNumber;
	
	public InstrumentMethodVisitor(MethodVisitor methodWriter, MethodMeta methodMeta) {
		this.mv = methodWriter;
		this.methodMeta = methodMeta;
		
		this.offset = 0;
		this.blockEnds = new HashSet<Integer>();
		jumpTargetLabels = new HashSet<Label>();
		conditionalLabels = new HashSet<Label>();

		this.lineNumber = 0;
		blockLines = new HashSet<Integer>();
	}

    public AnnotationVisitor visitAnnotationDefault() {
        return mv.visitAnnotationDefault();
    }

    public AnnotationVisitor visitAnnotation(
        final String desc,
        final boolean visible)
    {
        return mv.visitAnnotation(desc, visible);
    }

    public AnnotationVisitor visitParameterAnnotation(
        final int parameter,
        final String desc,
        final boolean visible)
    {
        return mv.visitParameterAnnotation(parameter, desc, visible);
    }

    public void visitAttribute(final Attribute attr) {
        mv.visitAttribute(attr);
    }

    public void visitCode() {
        mv.visitCode();
    }

    public void visitFrame(
        final int type,
        final int nLocal,
        final Object[] local,
        final int nStack,
        final Object[] stack)
    {
        mv.visitFrame(type, nLocal, local, nStack, stack);
    }

    public void visitInsn(final int opcode) {
		if (isReturn(opcode) || opcode == ATHROW) {
			addBlock(offset + 1);
		}

		mv.visitInsn(opcode);
		offset++;
    }

    public void visitIntInsn(final int opcode, final int operand) {
        mv.visitIntInsn(opcode, operand);
		offset++;
    }

    public void visitVarInsn(final int opcode, final int var) {
        mv.visitVarInsn(opcode, var);
		offset++;
    }

    public void visitTypeInsn(final int opcode, final String type) {
        mv.visitTypeInsn(opcode, type);
		offset++;
    }

    public void visitFieldInsn(
        final int opcode,
        final String owner,
        final String name,
        final String desc)
    {
        mv.visitFieldInsn(opcode, owner, name, desc);
		offset++;
    }

    public void visitMethodInsn(
        final int opcode,
        final String owner,
        final String name,
        final String desc)
    {
        mv.visitMethodInsn(opcode, owner, name, desc);
		offset++;
    }

    public void visitJumpInsn(final int opcode, final Label label) {
   		addBlock(offset + 1);
		
		jumpTargetLabels.add(label);
    	if (isConditionalBranch(opcode)) {
			methodMeta.addConditionalBranch();
    	}

		mv.visitJumpInsn(opcode, label);
		offset++;
    }

    public void visitLabel(final Label label) {
    	if (jumpTargetLabels.contains(label) && !blockEnds.contains(offset)) {
    		addBlock(offset);
    	}
    	if (conditionalLabels.contains(label)) {
    		methodMeta.addConditionalBranch();
    	}

    	mv.visitLabel(label);
    }

    public void visitLdcInsn(final Object cst) {
        mv.visitLdcInsn(cst);
		offset++;
    }

    public void visitIincInsn(final int var, final int increment) {
        mv.visitIincInsn(var, increment);
		offset++;
    }

    public void visitTableSwitchInsn(
        final int min,
        final int max,
        final Label dflt,
        final Label[] labels)
    {
        mv.visitTableSwitchInsn(min, max, dflt, labels);
		offset++;
    }

    public void visitLookupSwitchInsn(
        final Label dflt,
        final int[] keys,
        final Label[] labels)
    {
        mv.visitLookupSwitchInsn(dflt, keys, labels);
		offset++;
    }

    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        mv.visitMultiANewArrayInsn(desc, dims);
		offset++;
    }

    public void visitTryCatchBlock(
        final Label start,
        final Label end,
        final Label handler,
        final String type)
    {
        mv.visitTryCatchBlock(start, end, handler, type);
		
		conditionalLabels.add(handler);
		jumpTargetLabels.add(handler);
    }

    public void visitLocalVariable(
        final String name,
        final String desc,
        final String signature,
        final Label start,
        final Label end,
        final int index)
    {
        mv.visitLocalVariable(name, desc, signature, start, end, index);
    }

    public void visitLineNumber(final int line, final Label start) {
        mv.visitLineNumber(line, start);
    	
    	lineNumber = line;
    	blockLines.add(lineNumber);
    }

    public void visitMaxs(final int maxStack, final int maxLocals) {
    	mv.visitMaxs(maxStack + 2, maxLocals);
    }

    public void visitEnd() {
        mv.visitEnd();
    }

    void addBlock(int endOffset) {
    	if (blockLines.isEmpty()) {
    		blockLines.add(lineNumber);
    	}
		blockMeta = new BlockMeta(blockLines);
		blockLines = new HashSet<Integer>();
		methodMeta.addBlock(blockMeta);
		blockEnds.add(endOffset);
		
		installProbePoint();
    }
    
    void installProbePoint() {
		//Install probe
		//maxStack + 1
		mv.visitFieldInsn(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;");
		//maxStack + 1
		mv.visitLdcInsn(blockMeta.id().toString());
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/runtime/Probe", "touchBlock", "(Ljava/lang/String;)V");
    }
    
    boolean isConditionalBranch(int opcode) {
    	return Arrays.binarySearch(branchOpcodes, opcode) > -1;
    }
    
    boolean isReturn(int opcode) {
    	return Arrays.binarySearch(returnOpcodes, opcode) > -1;
    }
    
	private static final int[] branchOpcodes = new int[] {
		IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
		IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
		IF_ACMPEQ, IF_ACMPNE,
		IFNULL, IFNONNULL,
	};
	
	private static final int[] returnOpcodes = new int[] {
		IRETURN, LRETURN, FRETURN, DRETURN, ARETURN, RETURN
	};
	
	static {
		Arrays.sort(branchOpcodes);
		Arrays.sort(returnOpcodes);
	}
}