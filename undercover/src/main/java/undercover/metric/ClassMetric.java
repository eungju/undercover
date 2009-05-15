package undercover.metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import undercover.support.ObjectSupport;

public class ClassMetric extends ObjectSupport implements Serializable {
	private static final long serialVersionUID = -9154762635889065861L;

	private String name;
	private String source;
	private ArrayList<MethodMetric> methods;

	public ClassMetric(String name, String source) {
		this.name = name;
		this.source = source;
		this.methods = new ArrayList<MethodMetric>();
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

	public void addMethod(MethodMetric methodMetric) {
		methods.add(methodMetric);
	}

	public List<MethodMetric> methods() {
		return methods;
	}

	public MethodMetric getMethod(String name) {
		for (MethodMetric each : methods) {
			if (each.name().equals(name)) {
				return each;
			}
		}
		return null;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder(name);
		builder.append('{').append(source).append(',');
		builder.append('[');
		for (MethodMetric each : methods) {
			builder.append(each.toString()).append(',');
		}
		builder.append(']');
		builder.append('}');
		return builder.toString();
	}
}
