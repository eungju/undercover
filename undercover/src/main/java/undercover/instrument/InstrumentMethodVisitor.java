package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import undercover.metric.BlockMeta;
import undercover.metric.MethodMeta;

public class InstrumentMethodVisitor extends MethodNode {
	protected MethodVisitor methodVisitor;
	private MethodMeta methodMeta;

	public InstrumentMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, MethodMeta methodMeta, MethodVisitor methodVisitor) {
		super(access, name, desc, signature, exceptions);
		this.methodMeta = methodMeta;
		this.methodVisitor = methodVisitor;
	}

	int offset = 0;
	BlockMeta blockMeta = null;
	Set<Integer> blockEnds = new HashSet<Integer>();
	Set<Label> jumpTargetLabels = new HashSet<Label>();
	Set<Label> conditionalLabels = new HashSet<Label>();
	
	Set<Integer> blockLines = new HashSet<Integer>();
	int lineNumber = 0;

	public void visitEnd() {
		for (TryCatchBlockNode each : (List<TryCatchBlockNode>) tryCatchBlocks) {
			conditionalLabels.add(each.handler.getLabel());
			jumpTargetLabels.add(each.handler.getLabel());
		}
		
		InsnList instructions = this.instructions;
		for (Iterator<AbstractInsnNode> i = instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			System.out.println(each);
			if (each.getType() == AbstractInsnNode.INSN) {
				InsnNode node = (InsnNode) each;
				if (isReturn(node.getOpcode()) || node.getOpcode() == ATHROW) {
					addBlock(instructions, each, offset + 1);
				}
			} else if (each.getType() == AbstractInsnNode.JUMP_INSN) {
				JumpInsnNode node = (JumpInsnNode) each; 
				addBlock(instructions, each, offset + 1);
				
				jumpTargetLabels.add(node.label.getLabel());
		    	if (isConditionalBranch(node.getOpcode())) {
					methodMeta.addConditionalBranch();
		    	}
			} else if (each.getType() == AbstractInsnNode.LABEL) {
				LabelNode node = (LabelNode) each; 
		    	if (jumpTargetLabels.contains(node.getLabel()) && !blockEnds.contains(offset)) {
					addBlock(instructions, each, offset);
				}
				if (conditionalLabels.contains(node.getLabel())) {
					methodMeta.addConditionalBranch();
				}
			} else if (each.getType() == AbstractInsnNode.LINE) {
				LineNumberNode node = (LineNumberNode) each; 
		    	lineNumber = node.line;
		    	blockLines.add(lineNumber);
			}
			
			if (each.getOpcode() >= 0) {
				offset++;
			}
		}
    	maxStack += 2;
		accept(methodVisitor);
	}
	
    void addBlock(InsnList instructions, AbstractInsnNode location, int endOffset) {
    	if (blockLines.isEmpty()) {
    		blockLines.add(lineNumber);
    	}
		blockMeta = new BlockMeta(blockLines);
		blockLines = new HashSet<Integer>();
		methodMeta.addBlock(blockMeta);
		blockEnds.add(endOffset);
		
		installProbePoint(instructions, location);
    }
    
    void installProbePoint(InsnList instructions, AbstractInsnNode location) {
		//Install probe
    	InsnList code = new InsnList();
		//maxStack + 1
    	code.add(new FieldInsnNode(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;"));
		//maxStack + 1
    	code.add(new LdcInsnNode(blockMeta.id().toString()));
    	code.add(new MethodInsnNode(INVOKEVIRTUAL, "undercover/runtime/Probe", "touchBlock", "(Ljava/lang/String;)V"));
    	instructions.insertBefore(location, code);
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