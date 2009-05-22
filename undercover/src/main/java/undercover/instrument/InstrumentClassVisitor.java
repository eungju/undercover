package undercover.instrument;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import undercover.metric.ClassMeta;
import undercover.metric.MetaData;

public class InstrumentClassVisitor extends ClassAdapter {
	private MetaData metaData;
	private ClassMeta classMeta;
	private String className;

	public InstrumentClassVisitor(ClassWriter classWriter, MetaData metaData) {
		super(classWriter);
		this.metaData = metaData;
	}
	
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className = name;
	}
	
	public void visitSource(String source, String debug) {
		super.visitSource(source, debug);
		classMeta = new ClassMeta(className, source);
		metaData.addClass(classMeta);
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature,	exceptions);
		if (mv != null) {
			mv = new InstrumentMethodVisitor(access, name, desc, signature, exceptions, classMeta, mv);
		}
		return mv;
	}
}