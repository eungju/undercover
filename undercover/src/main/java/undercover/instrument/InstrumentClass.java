package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.ClassMetric;
import undercover.metric.BlockMetric;
import undercover.metric.MetaDataCollector;
import undercover.metric.MethodMetric;

public class InstrumentClass extends ClassAdapter {
	private MetaDataCollector collector;
	private ClassMetric classMetric;
	private String className;

	public InstrumentClass(ClassWriter classWriter, MetaDataCollector metricCollector) {
		super(classWriter);
		this.collector = metricCollector;
	}
	
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className = name;
	}
	
	public void visitSource(String source, String debug) {
		super.visitSource(source, debug);
		classMetric = new ClassMetric(className, source);
		collector.defineClass(classMetric);
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,	exceptions);
		if (mv != null) {
			MethodMetric methodMetric = new MethodMetric(name, desc);
			classMetric.addMethod(methodMetric);
			mv = new InstrumentMethod(mv, collector, methodMetric);
		}
		return mv;
	}

	public static class InstrumentMethod extends MethodAdapter {
		private MetaDataCollector collector;
		private MethodMetric methodMetric;
		private BlockMetric blockMetric;

		public InstrumentMethod(MethodVisitor methodWriter, MetaDataCollector metricCollector, MethodMetric methodMetric) {
			super(methodWriter);
			this.collector = metricCollector;
			this.methodMetric = methodMetric;
			blockMetric = new BlockMetric();
			methodMetric.addFragment(blockMetric);
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
		
	    public void visitJumpInsn(int opcode, Label label) {
	    	super.visitJumpInsn(opcode, label);
	        if (Arrays.binarySearch(branchOpcodes, opcode) > -1) {
				blockMetric = new BlockMetric();
				methodMetric.addFragment(blockMetric);
	        }
	    }
	}
}