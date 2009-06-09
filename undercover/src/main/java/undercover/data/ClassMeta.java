package undercover.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class ClassMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -9154762635889065861L;

	public final String name;
	public final String source;
	public final List<MethodMeta> methods;
	public final Outer outer;
	
	public ClassMeta(String name, String source) {
		this(name, source, new ArrayList<MethodMeta>());
	}
	
	public ClassMeta(String name, String source, List<MethodMeta> methods) {
		this(name, source, methods, null);
	}
	
	public ClassMeta(String name, String source, List<MethodMeta> methods, Outer outer) {
		this.name = name;
		this.source = source;
		this.methods = methods;
		this.outer = outer;
	}

	public String getPackageName() {
		int lastSeparator = name.lastIndexOf('/');
		return lastSeparator == -1 ? name : name.substring(0, lastSeparator);
	}

	public String getExpectedSourcePath() {
		return getPackageName() + "/" + source;
	}

	public MethodMeta getMethod(String name, String descriptor) {
		for (MethodMeta each : methods) {
			if (each.name.equals(name) && each.descriptor.equals(descriptor)) {
				return each;
			}
		}
		return null;
	}
	
	public void accept(MetaDataVisitor visitor) {
		visitor.visitEnter(this);
		for (MethodMeta each : methods) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public boolean isAnonymous() {
		return outer != null;
	}
	
	public static class Outer implements Serializable {
		private static final long serialVersionUID = 1160336265135937380L;
		
		public final String className;
		public final String methodName;
		
		public Outer(String className, String methodName) {
			this.className = className;
			this.methodName = methodName;
		}
		
		public boolean isMethod() {
			return methodName != null;
		}
	}
}
