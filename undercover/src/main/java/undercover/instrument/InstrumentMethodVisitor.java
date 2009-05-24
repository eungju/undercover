package undercover.instrument;

import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import undercover.metric.MethodMeta;

public class InstrumentMethodVisitor extends MethodNode {
	private MethodVisitor methodVisitor;
	private String className;
	private List<MethodMeta> methodMetas;

	public InstrumentMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor methodVisitor, String className, List<MethodMeta> methodMetas) {
		super(access, name, desc, signature, exceptions);
		this.methodVisitor = methodVisitor;
		this.className = className;
		this.methodMetas = methodMetas;
	}

	public void visitEnd() {
		BasicBlockAnalyzer analyzer = new BasicBlockAnalyzer();
		analyzer.analyze(this);
		MethodMeta methodMeta = analyzer.instrument(this, className, methodMetas.size());
		methodMetas.add(methodMeta);
		accept(methodVisitor);
	}
}