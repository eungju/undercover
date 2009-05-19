package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.BlockMeta;
import undercover.metric.MethodMeta;

public class InstrumentMethodVisitor extends MethodAdapter {
	private MethodMeta methodMeta;
	private BlockMeta blockMeta;
	private int lineNumber;
	
	private Set<Label> jumpTargetLabels;
	private Set<Label> conditionalLabels;
	
	public InstrumentMethodVisitor(MethodVisitor methodWriter, MethodMeta methodMeta) {
		super(methodWriter);
		jumpTargetLabels = new HashSet<Label>();
		conditionalLabels = new HashSet<Label>();
		this.methodMeta = methodMeta;
		this.lineNumber = 0;
	}
	
	public void visitCode() {
		super.visitCode();
		
		addBlock();
	}
	
	public void visitJumpInsn(int opcode, Label label) {
    	super.visitJumpInsn(opcode, label);
    	
    	if (isConditionalBranch(opcode)) {
			methodMeta.addConditionalBranch();
			addBlock();
    	}
		jumpTargetLabels.add(label);
    }
	
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		
		conditionalLabels.add(handler);
		jumpTargetLabels.add(handler);
	}
	
    public void visitLabel(Label label) {
    	super.visitLabel(label);
    	
    	if (conditionalLabels.contains(label)) {
    		methodMeta.addConditionalBranch();
    	}
    	if (jumpTargetLabels.contains(label)) {
    		addBlock();
    	}
    }
    
    public void visitMaxs(int maxStack, int maxLocals) {
    	super.visitMaxs(maxStack + 2, maxLocals);
    }
    
    public void visitLineNumber(int line, Label start) {
    	super.visitLineNumber(line, start);
    	
    	lineNumber = line;
    	blockMeta.addLine(lineNumber);
    }
	
    void addBlock() {
		blockMeta = new BlockMeta();
		if (lineNumber > 0) {
			blockMeta.addLine(lineNumber);
		}
		methodMeta.addBlock(blockMeta);
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
    
	private static final int[] branchOpcodes = new int[] {
		IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
		IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
		IF_ACMPEQ, IF_ACMPNE,
		IFNULL, IFNONNULL,
	};
	
	static {
		Arrays.sort(branchOpcodes);
	}
}