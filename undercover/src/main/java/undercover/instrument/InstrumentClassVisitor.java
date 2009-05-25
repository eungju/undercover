package undercover.instrument;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import undercover.metric.ClassMeta;
import undercover.metric.MetaData;
import undercover.metric.MethodMeta;

public class InstrumentClassVisitor extends ClassNode {
	private final MetaData metaData;
	
	public InstrumentClassVisitor(MetaData metaData) {
		super();
		this.metaData = metaData;
	}
	
	public void visitEnd() {
		super.visitEnd();
		
		if ((access & ACC_SYNTHETIC) != 0) {
			return;
		}
		
		List<MethodMeta> methodMetas = new ArrayList<MethodMeta>();
		MethodNode clinitMethod = null;
		for (MethodNode each : (List<MethodNode>) methods) {
			if (each.name.equals("<clinit>")) {
				clinitMethod = each;
			}
			if ((each.access & ACC_SYNTHETIC) != 0) {
				continue;
			}
			BasicBlockAnalyzer analyzer = new BasicBlockAnalyzer();
			analyzer.analyze(each);
			MethodMeta methodMeta = analyzer.instrument(each, name, methodMetas.size());
			methodMetas.add(methodMeta);
		}
		
		ClassMeta classMeta = new ClassMeta(name, sourceFile, methodMetas);
		metaData.addClass(classMeta);
		
		addCoverageField();
		addClinitMethod(name, methodMetas, clinitMethod);
	}
	
	void addCoverageField() {
		//Should be "public static final" for interfaces
		fields.add(new FieldNode(ACC_SYNTHETIC | ACC_PUBLIC | ACC_FINAL | ACC_STATIC, Instrument.COVERAGE_FIELD_NAME, "[[I", null, null));
	}

	void addClinitMethod(String className, List<MethodMeta> methodMetas, MethodNode clinitMethod) {
		InsnList code = new InsnList();
		code.add(new IntInsnNode(SIPUSH, methodMetas.size()));
		code.add(new TypeInsnNode(ANEWARRAY, "[I"));
		code.add(new FieldInsnNode(PUTSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I"));
		int methodIndex = 0;
		for (MethodMeta each : methodMetas) {
			code.add(new FieldInsnNode(GETSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I"));
			code.add(new IntInsnNode(SIPUSH, methodIndex));
			code.add(new IntInsnNode(SIPUSH, each.blocks.size()));
			code.add(new IntInsnNode(NEWARRAY, T_INT));
			code.add(new InsnNode(AASTORE));
			methodIndex++;
		}
		code.add(new FieldInsnNode(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;"));
		code.add(new LdcInsnNode(className));
		code.add(new FieldInsnNode(GETSTATIC, className, Instrument.COVERAGE_FIELD_NAME, "[[I"));
		code.add(new MethodInsnNode(INVOKEVIRTUAL, "undercover/runtime/Probe", "register", "(Ljava/lang/String;[[I)V"));

		if (clinitMethod == null) {
			clinitMethod = new MethodNode(ACC_SYNTHETIC | ACC_STATIC, "<clinit>", "()V", null, null);
			methods.add(clinitMethod);
			code.add(new InsnNode(RETURN));
		}
		
		clinitMethod.instructions.insert(code);
		clinitMethod.maxStack += 4;
	}
}