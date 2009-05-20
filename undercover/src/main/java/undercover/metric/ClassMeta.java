package undercover.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class ClassMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -9154762635889065861L;

	private String name;
	private String source;
	private ArrayList<MethodMeta> methods;

	public ClassMeta(String name, String source) {
		this.name = name;
		this.source = source;
		this.methods = new ArrayList<MethodMeta>();
	}
	
	public String name() {
		return name;
	}
	
	public String source() {
		return source;
	}
	
	public String getPackageName() {
		int lastSeparator = name.lastIndexOf('/');
		return lastSeparator == -1 ? name : name.substring(0, lastSeparator);
	}

	public void addMethod(MethodMeta methodMeta) {
		methods.add(methodMeta);
	}

	public List<MethodMeta> methods() {
		return methods;
	}

	public MethodMeta getMethod(String name) {
		for (MethodMeta each : methods) {
			if (each.name().equals(name)) {
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
}
