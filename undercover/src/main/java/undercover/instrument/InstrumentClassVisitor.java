package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.ClassMeta;
import undercover.metric.MetaData;
import undercover.metric.MethodMeta;

public class InstrumentClassVisitor extends ClassAdapter {
	private final MetaData metaData;
	private String className;
	private String source;
	private List<MethodMeta> methodMetas;
	boolean clinitIsInstrumented = false;
	
	public InstrumentClassVisitor(ClassWriter classWriter, MetaData metaData) {
		super(classWriter);
		this.metaData = metaData;
		this.methodMetas = new ArrayList<MethodMeta>();
	}
	
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className = name;
	}
	
	public void visitEnd() {
		metaData.addClass(new ClassMeta(className, source, methodMetas));

		addCoverageField();
		addPreClinitMethod();		
		if (!clinitIsInstrumented) {
			addClinitMethod();
		}
	}

	void addClinitMethod() {
		MethodVisitor mv = super.visitMethod(ACC_SYNTHETIC + ACC_STATIC, "<clinit>", "()V", null, null);
		mv.visitCode();
		mv.visitMethodInsn(INVOKESTATIC, className, Instrument.PRE_CLINIT_METHOD_NAME, "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	void addPreClinitMethod() {
		MethodVisitor mv = super.visitMethod(ACC_SYNTHETIC + ACC_PRIVATE + ACC_STATIC, Instrument.PRE_CLINIT_METHOD_NAME, "()V", null, null);
		mv.visitCode();
		mv.visitIntInsn(SIPUSH, methodMetas.size());
		mv.visitTypeInsn(ANEWARRAY, "[I");
		mv.visitFieldInsn(PUTSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I");
		int methodIndex = 0;
		for (MethodMeta each : methodMetas) {
			mv.visitFieldInsn(GETSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I");
			mv.visitIntInsn(SIPUSH, methodIndex);
			mv.visitIntInsn(SIPUSH, each.blocks.size());
			mv.visitIntInsn(NEWARRAY, T_INT);
			mv.visitInsn(AASTORE);
			methodIndex++;
		}
		mv.visitFieldInsn(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;");
		mv.visitLdcInsn(className);
		mv.visitFieldInsn(GETSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I");
		mv.visitMethodInsn(INVOKEVIRTUAL, "undercover/runtime/Probe", "register", "(Ljava/lang/String;[[I)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(3, 0);
		mv.visitEnd();
	}

	void addCoverageField() {
		FieldVisitor fv = super.visitField(ACC_SYNTHETIC + ACC_PUBLIC + ACC_STATIC, Instrument.COVERAGE_FIELD_NAME, "[[I", null, null);
		fv.visitEnd();
	}
	
	public void visitSource(String source, String debug) {
		super.visitSource(source, debug);
		this.source = source;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,	exceptions);
		if (mv != null) {
			mv = new InstrumentMethodVisitor(access, name, desc, signature, exceptions, mv, className, methodMetas);
			if (name.equals("<clinit>")) {
				clinitIsInstrumented = true;
			}
		}
		return mv;
	}
}