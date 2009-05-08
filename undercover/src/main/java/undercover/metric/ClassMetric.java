package undercover.metric;

import java.util.ArrayList;

public class ClassMetric {
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

	public void addMethod(MethodMetric methodMetric) {
		methods.add(methodMetric);
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
