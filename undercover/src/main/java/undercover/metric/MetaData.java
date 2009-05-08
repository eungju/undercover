package undercover.metric;

import java.util.List;

public class MetaData {
	private List<ClassMetric> classes;

	public MetaData(List<ClassMetric> classes) {
		this.classes = classes;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ClassMetric each : classes) {
			builder.append(each.toString()).append(',');
		}
		return builder.toString();
	}
}
