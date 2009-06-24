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

import undercover.data.ClassMeta;
import undercover.data.MethodMeta;
import undercover.instrument.filter.Exclusion;

public class ClassAnalyzer {
	private final Exclusion exclusion;
	private final AnonymousDetector anonymousDetector;
	
	public ClassAnalyzer(Exclusion exclusion) {
		this.exclusion = exclusion;
		anonymousDetector = new AnonymousDetector();
	}
	
	public ClassMeta instrument(ClassNode classNode) {
		ClassMeta.Outer outer = anonymousDetector.inspect(classNode);
		
		List<MethodMeta> methodMetas = new ArrayList<MethodMeta>();
		for (MethodNode each : (List<MethodNode>) classNode.methods) {
			if (exclusion.exclude(classNode, each)) {
				continue;
			}
			BasicBlockAnalyzer analyzer = new BasicBlockAnalyzer();
			analyzer.analyze(each);
			MethodMeta methodMeta = analyzer.instrument(each, classNode.name, methodMetas.size());
			methodMetas.add(methodMeta);
		}
		
		ClassMeta classMeta = new ClassMeta(classNode.name, classNode.sourceFile, methodMetas, outer);
		
		addCoverageField(classNode);
		addCoverageFieldInitializer(classNode, methodMetas);
		
		return classMeta;
	}
	
	void addCoverageField(ClassNode classNode) {
		//Should be "public static final" for interfaces
		classNode.fields.add(new FieldNode(ACC_SYNTHETIC | ACC_PUBLIC | ACC_FINAL | ACC_STATIC, Instrument.BLOCK_COVERAGE_FIELD_NAME, "[[I", null, null));
	}

	void addCoverageFieldInitializer(ClassNode classNode, List<MethodMeta> methodMetas) {
		InsnList code = new InsnList();
		code.add(new IntInsnNode(SIPUSH, methodMetas.size()));
		code.add(new TypeInsnNode(ANEWARRAY, "[I"));
		code.add(new FieldInsnNode(PUTSTATIC, classNode.name, Instrument.BLOCK_COVERAGE_FIELD_NAME, "[[I"));
		int methodIndex = 0;
		for (MethodMeta each : methodMetas) {
			code.add(new FieldInsnNode(GETSTATIC, classNode.name, Instrument.BLOCK_COVERAGE_FIELD_NAME, "[[I"));
			code.add(new IntInsnNode(SIPUSH, methodIndex));
			code.add(new IntInsnNode(SIPUSH, each.blocks.size()));
			code.add(new IntInsnNode(NEWARRAY, T_INT));
			code.add(new InsnNode(AASTORE));
			methodIndex++;
		}
		code.add(new FieldInsnNode(GETSTATIC, "undercover/runtime/Probe", "INSTANCE", "Lundercover/runtime/Probe;"));
		code.add(new LdcInsnNode(classNode.name));
		code.add(new FieldInsnNode(GETSTATIC, classNode.name, Instrument.BLOCK_COVERAGE_FIELD_NAME, "[[I"));
		code.add(new MethodInsnNode(INVOKEVIRTUAL, "undercover/runtime/Probe", "register", "(Ljava/lang/String;[[I)V"));

		MethodNode clinitMethod = findClassInitializer(classNode);
		if (clinitMethod == null) {
			clinitMethod = new MethodNode(ACC_SYNTHETIC | ACC_STATIC, "<clinit>", "()V", null, null);
			classNode.methods.add(clinitMethod);
			code.add(new InsnNode(RETURN));
		}
		
		clinitMethod.instructions.insert(code);
		clinitMethod.maxStack += 4;
	}

	MethodNode findClassInitializer(ClassNode classNode) {
		for (MethodNode each : (List<MethodNode>) classNode.methods) {
			if (each.name.equals("<clinit>")) {
				return each;
			}
		}
		return null;
	}
}
