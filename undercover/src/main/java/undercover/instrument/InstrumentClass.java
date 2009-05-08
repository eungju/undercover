package undercover.instrument;

import java.util.Arrays;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.ClassMetric;
import undercover.metric.FragmentMetric;
import undercover.metric.MethodMetric;
import undercover.metric.MetricCollector;

import static org.objectweb.asm.Opcodes.*;

public class InstrumentClass extends ClassAdapter {
	private MetricCollector collector;
	private ClassMetric classMetric;
	private String className;

	public InstrumentClass(ClassWriter classWriter, MetricCollector metricCollector) {
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
			collector.defineMethod(methodMetric);
			classMetric.addMethod(methodMetric);
			mv = new InstrumentMethod(mv, collector, methodMetric);
		}
		return mv;
	}

	public static class InstrumentMethod extends MethodAdapter {
		private MetricCollector collector;
		private MethodMetric methodMetric;
		private FragmentMetric fragmentMetric;

		public InstrumentMethod(MethodVisitor methodWriter, MetricCollector metricCollector, MethodMetric methodMetric) {
			super(methodWriter);
			this.collector = metricCollector;
			this.methodMetric = methodMetric;
		}
		
		public void visitLineNumber(int line, Label start) {
			super.visitLineNumber(line, start);
			fragmentMetric = new FragmentMetric(line);
			collector.defineFragment(fragmentMetric);
			methodMetric.addFragment(fragmentMetric);
		}

		private static final int[] branchOpcodes = new int[] {
			IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
			IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
			IF_ACMPEQ, IF_ACMPNE,
			IFNULL, IFNONNULL
		};
		
		static {
			Arrays.sort(branchOpcodes);
		}
		
	    public void visitJumpInsn(int opcode, Label label) {
	    	super.visitJumpInsn(opcode, label);
	        if (Arrays.binarySearch(branchOpcodes, opcode) > -1) {
		        fragmentMetric.foundBranch();
	        }
	    }
	}
}