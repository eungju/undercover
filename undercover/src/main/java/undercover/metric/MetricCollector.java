package undercover.metric;

import java.util.ArrayList;
import java.util.List;

public class MetricCollector {
	private List<ClassMetric> classes = new ArrayList<ClassMetric>();
	private List<MethodMetric> methods = new ArrayList<MethodMetric>();
	private List<FragmentMetric> fragments = new ArrayList<FragmentMetric>();

	public int defineClass(ClassMetric classMetric) {
		classes.add(classMetric);
		return classes.size() - 1;
	}
	
	public int defineMethod(MethodMetric methodMetric) {
		methods.add(methodMetric);
		return methods.size() - 1;
	}
	
	public int defineFragment(FragmentMetric fragmentMetric) {
		fragments.add(fragmentMetric);
		return fragments.size() - 1;
	}
	
	public MetaData getMetaData() {
		return new MetaData(classes);
	}

	public void touchFragment(int fragmentId) {
		fragments.get(fragmentId).touch();
	}
}
