package undercover.metric;

import java.util.ArrayList;
import java.util.List;

public class MetaDataCollector {
	private List<ClassMetric> classes = new ArrayList<ClassMetric>();

	public int defineClass(ClassMetric classMetric) {
		classes.add(classMetric);
		return classes.size() - 1;
	}
	
	public MetaData getMetaData() {
		return new MetaData(classes);
	}
}
