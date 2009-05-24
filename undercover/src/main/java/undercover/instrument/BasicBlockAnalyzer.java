package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import undercover.metric.BlockMeta;
import undercover.metric.MethodMeta;

public class BasicBlockAnalyzer {
	public List<BasicBlock> blocks = new ArrayList<BasicBlock>();
	public int complexity = 1;
	
	private Set<Label> targetLabels = new HashSet<Label>();
	private BasicBlock basicBlock = null;
	private int lineNumber = 0;
	
	public void analyze(MethodNode methodNode) {
		scanJumpTargets(methodNode);
		scanBlocks(methodNode);
	}
	
	void scanBlocks(MethodNode methodNode) {
		int offset = 0;
		basicBlock = new BasicBlock(offset);
		for (Iterator<AbstractInsnNode> i = methodNode.instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			if (each.getType() == AbstractInsnNode.INSN) {
				if (isReturn(each.getOpcode()) || each.getOpcode() == ATHROW) {
					addBasicBlock(offset + 1);
				}
			} else if (each.getType() == AbstractInsnNode.VAR_INSN) {
				if (each.getOpcode() == RET) {
					addBasicBlock(offset + 1);
				}
			} else if (each.getType() == AbstractInsnNode.JUMP_INSN) {
				addBasicBlock(offset + 1);
				if (isConditionalBranch(each.getOpcode())) {
					complexity++;
				}
			} else if (each.getType() == AbstractInsnNode.TABLESWITCH_INSN) {
				TableSwitchInsnNode node = (TableSwitchInsnNode) each;
				addBasicBlock(offset + 1);
				complexity += node.labels.size();
			} else if (each.getType() == AbstractInsnNode.LOOKUPSWITCH_INSN) {
				LookupSwitchInsnNode node = (LookupSwitchInsnNode) each;
				addBasicBlock(offset + 1);
				complexity += node.labels.size();
			} else if (each.getType() == AbstractInsnNode.LABEL) {
				LabelNode node = (LabelNode) each;
				if (targetLabels.contains(node.getLabel()) && basicBlock.start < offset) {
					addBasicBlock(offset);
				}
			} else if (each.getType() == AbstractInsnNode.LINE) {
				LineNumberNode node = (LineNumberNode) each;
				lineNumber = node.line;
				basicBlock.lines.add(lineNumber);
			}
			
			if (each.getOpcode() != -1) {
				offset++;
			}
		}	
	}
	
	void addBasicBlock(int nextOffset) {
		basicBlock.end = nextOffset;
		if (basicBlock.lines.isEmpty() && lineNumber > 0) {
			basicBlock.lines.add(lineNumber);
		}
		blocks.add(basicBlock);
		basicBlock = new BasicBlock(nextOffset);
	}

	void scanJumpTargets(MethodNode methodNode) {
		for (TryCatchBlockNode each : (Collection<TryCatchBlockNode>) methodNode.tryCatchBlocks) {
			targetLabels.add(each.handler.getLabel());
		}
		int offset = 0;
		for (Iterator<AbstractInsnNode> i = methodNode.instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			if (each.getType() == AbstractInsnNode.JUMP_INSN) {
				JumpInsnNode node = (JumpInsnNode) each;
				targetLabels.add(node.label.getLabel());
			} else if (each.getType() == AbstractInsnNode.TABLESWITCH_INSN) {
				TableSwitchInsnNode node = (TableSwitchInsnNode) each;
				targetLabels.add(node.dflt.getLabel());
				for (LabelNode labelNode : (List<LabelNode>) node.labels) {
					targetLabels.add(labelNode.getLabel());
				}
			} else if (each.getType() == AbstractInsnNode.LOOKUPSWITCH_INSN) {
				LookupSwitchInsnNode node = (LookupSwitchInsnNode) each;
				targetLabels.add(node.dflt.getLabel());
				for (LabelNode labelNode : (List<LabelNode>) node.labels) {
					targetLabels.add(labelNode.getLabel());
				}
			}
			
			if (each.getOpcode() != -1) {
				offset++;
			}
		}
	}
	
	public MethodMeta instrument(MethodNode methodNode, String className, int methodIndex) {
		MethodMeta methodMeta = new MethodMeta(methodNode.name + methodNode.desc, complexity);
		Iterator<BasicBlock> cursor = blocks.iterator();
		if (!cursor.hasNext()) {
			return methodMeta;
		}
		
		BasicBlock block = cursor.next();
		int blockIndex = 0;
		int offset = 0;
		for (Iterator<AbstractInsnNode> i = methodNode.instructions.iterator(); i.hasNext(); ) {
			AbstractInsnNode each = i.next();
			if (each.getOpcode() == -1) {
				continue;
			}
			
			if (block.end - 1 == offset) {
				BlockMeta blockMeta = new BlockMeta(block.lines);
				methodMeta.addBlock(blockMeta);
				installProbePoint(methodNode.instructions, each, blockMeta, className, methodIndex, blockIndex);
				if (cursor.hasNext()) {
					block = cursor.next();
					blockIndex++;
				}
			}
			
			offset++;
		}
		
		if (methodNode.name.equals("<clinit>")) {
			methodNode.instructions.insert(new MethodInsnNode(INVOKESTATIC, className, Instrument.PRE_CLINIT_METHOD_NAME, "()V"));
		}
		
		methodNode.maxStack += 4;
		return methodMeta;
	}

    void installProbePoint(InsnList instructions, AbstractInsnNode location, BlockMeta blockMeta, String className, int methodIndex, int blockIndex) {
       	InsnList ecode = new InsnList();
       	ecode.add(new FieldInsnNode(GETSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I"));
       	ecode.add(new IntInsnNode(SIPUSH, methodIndex));
       	ecode.add(new InsnNode(AALOAD));
       	ecode.add(new IntInsnNode(SIPUSH, blockIndex));
       	ecode.add(new InsnNode(DUP2));
       	ecode.add(new InsnNode(IALOAD));
       	ecode.add(new InsnNode(ICONST_1));
       	ecode.add(new InsnNode(IADD));
       	ecode.add(new InsnNode(IASTORE));
    	instructions.insertBefore(location, ecode);
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
