package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

public class BasicBlockAnalyzer {
	public List<BasicBlock> blocks = new ArrayList<BasicBlock>();

	private Set<Label> targetLabels = new HashSet<Label>();
	private Map<Label, Integer> labelOffsets = new HashMap<Label, Integer>();
	private BasicBlock basicBlock = null;

	public void analyze(MethodNode methodNode) {
		InsnList instructions = methodNode.instructions;
		
		scanJumpTargets(methodNode, instructions);
		
		int offset = 0;
		basicBlock = new BasicBlock(offset);
		for (Iterator<AbstractInsnNode> i = instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			int nextOffset = offset + 1;
			if (each.getType() == AbstractInsnNode.INSN) {
				if (isReturn(each.getOpcode()) || each.getOpcode() == ATHROW) {
					basicBlock.end = nextOffset;
					blocks.add(basicBlock);
					basicBlock = new BasicBlock(nextOffset);
				}
			} else if (each.getType() == AbstractInsnNode.JUMP_INSN) {
				JumpInsnNode node = (JumpInsnNode) each;
				basicBlock.end = nextOffset;
				basicBlock.addSuccessor(labelOffsets.get(node.label.getLabel()));
				if (isConditionalBranch(each.getOpcode())) {
					basicBlock.addSuccessor(nextOffset);
				}
				blocks.add(basicBlock);
				basicBlock = new BasicBlock(nextOffset);
			} else if (each.getType() == AbstractInsnNode.LABEL) {
				LabelNode node = (LabelNode) each;
				if (targetLabels.contains(node.getLabel()) && basicBlock.start < offset) {
					basicBlock.end = offset;
					basicBlock.addSuccessor(offset);
					blocks.add(basicBlock);
					basicBlock = new BasicBlock(offset);
				}
			}
			
			if (each.getOpcode() != -1) {
				offset++;
			}
		}
	}

	void scanJumpTargets(MethodNode methodNode, InsnList instructions) {
		for (TryCatchBlockNode each : (Collection<TryCatchBlockNode>) methodNode.tryCatchBlocks) {
			targetLabels.add(each.handler.getLabel());
		}
		int offset = 0;
		for (Iterator<AbstractInsnNode> i = instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			if (each.getType() == AbstractInsnNode.JUMP_INSN) {
				targetLabels.add(((JumpInsnNode) each).label.getLabel());
			} else if (each.getType() == AbstractInsnNode.LABEL) {
				LabelNode node = (LabelNode) each;
				labelOffsets.put(node.getLabel(), offset);
			}
			
			if (each.getOpcode() > -1) {
				offset++;
			}
		}
	}
	
	public int calculateComplexity() {
		int edges = 0; 
		int nodes = 0; 
		for (BasicBlock each : blocks) { 
			edges += each.successors.size(); 
			nodes += 1; 
		} 
		return edges - nodes + 2;
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
