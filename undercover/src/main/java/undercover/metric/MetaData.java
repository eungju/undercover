package undercover.metric;

import java.util.List;

public class MetaData {
	private List<ClassMetric> classes;

	public MetaData(List<ClassMetric> classes) {
		this.classes = classes;
	}

	public int addClass(ClassMetric classMetric) {
		classes.add(classMetric);
		return classes.size() - 1;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ClassMetric each : classes) {
			builder.append(each.toString()).append(',');
		}
		return builder.toString();
	}
}
