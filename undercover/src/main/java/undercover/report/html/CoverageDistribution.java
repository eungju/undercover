package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;

public class CoverageDistribution {
	private int[] counts = new int[10];
	
	public CoverageDistribution(Collection<ClassItem> items) {
		for (ClassItem each : items) {
			count(each);
		}
	}
	
	void count(ClassItem item) {
		counts[coverageInterval(item.getCoverageRate())]++;
	}
	
	public int[] getCounts() {
		return counts;
	}

	static int coverageInterval(double coverageRate) {
		int i = (int) Math.floor(coverageRate * 10);
		return i == 10 ? 9 : i;
	}
}
