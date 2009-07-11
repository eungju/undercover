package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;

public class CoverageDistribution {
	private final int[] counts;
	
	public CoverageDistribution(Collection<ClassItem> items) {
		this();
		countAll(items);
	}
	
	public CoverageDistribution() {
		counts = new int[10];
	}

	public void count(ClassItem item) {
		counts[coverageInterval(item.getBlockMetrics().getCoverage().getRatio())]++;
	}
	
	public void countAll(Collection<ClassItem> items) {
		for (ClassItem each : items) {
			count(each);
		}
	}
	
	public int[] getCounts() {
		return counts;
	}

	static int coverageInterval(double coverageRate) {
		int i = (int) Math.floor(coverageRate * 10);
		return i == 10 ? 9 : i;
	}
}
