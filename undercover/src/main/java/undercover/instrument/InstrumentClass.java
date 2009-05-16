package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.BlockMetric;
import undercover.metric.ClassMetric;
import undercover.metric.MetaData;
import undercover.metric.MethodMetric;

public class InstrumentClass extends ClassAdapter {
	private MetaData metaData;
	private ClassMetric classMetric;
	private String className;

	public InstrumentClass(ClassWriter classWriter, MetaData metaData) {
		super(classWriter);
		this.metaData = metaData;
	}
	
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className = name;
	}
	
	public void visitSource(String source, String debug) {
		super.visitSource(source, debug);
		classMetric = new ClassMetric(className, source);
		metaData.addClass(classMetric);
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,	exceptions);
		if (mv != null) {
			MethodMetric methodMetric = new MethodMetric(name + desc);
			classMetric.addMethod(methodMetric);
			mv = new InstrumentMethod(mv, methodMetric);
		}
		return mv;
	}

	public static class InstrumentMethod extends MethodAdapter {
		private MethodMetric methodMetric;
		private BlockMetric blockMetric;
		
		private Set<Label> blockLabels;
		private Set<Label> conditionalLabels;
		
		public InstrumentMethod(MethodVisitor methodWriter, MethodMetric methodMetric) {
			super(methodWriter);
			blockLabels = new HashSet<Label>();
			conditionalLabels = new HashSet<Label>();
			this.methodMetric = methodMetric;
		}
		
		public void visitCode() {
    		addBlock();
		}
		
		public void visitJumpInsn(int opcode, Label label) {
	    	super.visitJumpInsn(opcode, label);
	    	if (isConditionalBranch(opcode)) {
				methodMetric.addConditionalBranch();
				addBlock();
	    	}
			blockLabels.add(label);
	    }
		
		public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
			conditionalLabels.add(handler);
			blockLabels.add(handler);
		}
		
        public void visitLabel(Label label) {
        	if (conditionalLabels.contains(label)) {
        		methodMetric.addConditionalBranch();
        	}
        	if (blockLabels.contains(label)) {
        		addBlock();
        	}
        }
	    
	    public void visitMaxs(int maxStack, int maxLocals) {
	    	super.visitMaxs(maxStack + 2, maxLocals);
	    }
		
	    void addBlock() {
			blockMetric = new BlockMetric();
			methodMetric.addBlock(blockMetric);
			
			//Install probe
			//maxStack + 1
			mv.visitFieldInsn(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;");
			//maxStack + 1
			mv.visitLdcInsn(blockMetric.id().toString());
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
}