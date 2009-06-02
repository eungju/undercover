package undercover.metric;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import undercover.support.ObjectSupport;

public class ClassMeta extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -9154762635889065861L;

	public final String name;
	public final String source;
	public final List<MethodMeta> methods;

	public ClassMeta(String name, String source) {
		this(name, source, Collections.<MethodMeta>emptyList());
	}
	
	public ClassMeta(String name, String source, List<MethodMeta> methods) {
		this.name = name;
		this.source = source;
		this.methods = methods;
	}

	public String getPackageName() {
		int lastSeparator = name.lastIndexOf('/');
		return lastSeparator == -1 ? name : name.substring(0, lastSeparator);
	}

	public String getExpectedSourcePath() {
		return getPackageName() + "/" + source;
	}

	public MethodMeta getMethod(String name) {
		for (MethodMeta each : methods) {
			if (each.name.equals(name)) {
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
