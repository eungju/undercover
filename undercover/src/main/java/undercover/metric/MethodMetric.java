package undercover.metric;

import java.util.ArrayList;
import java.util.List;

public class MethodMetric {
	private String name;
	private String descriptor;
	private List<BlockMetric> fragments;

	public MethodMetric(String name, String descriptor) {
		this.name = name;
		this.descriptor = descriptor;
		fragments = new ArrayList<BlockMetric>();
	}
	
	public String name() {
		return name;
	}
	
	public void addFragment(BlockMetric fragmentMetric) {
		fragments.add(fragmentMetric);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(name).append(',').append(descriptor).append(',');
		builder.append('{');
		builder.append('[');
		for (BlockMetric each : fragments) {
			builder.append(each.toString()).append(',');
		}
		builder.append(']');
		builder.append('}');
		return builder.toString();
	}
}
