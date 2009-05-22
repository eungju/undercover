package undercover.instrument;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import undercover.metric.ClassMeta;

public class InstrumentMethodVisitor extends MethodNode {
	private MethodVisitor methodVisitor;
	private ClassMeta classMeta;

	public InstrumentMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, ClassMeta classMeta, MethodVisitor methodVisitor) {
		super(access, name, desc, signature, exceptions);
		this.classMeta = classMeta;
		this.methodVisitor = methodVisitor;
	}

	public void visitEnd() {
		BasicBlockAnalyzer analyzer = new BasicBlockAnalyzer();
		analyzer.analyze(this);
		classMeta.addMethod(analyzer.instrument(this));
		accept(methodVisitor);
	}
}