package undercover.report.html;

import java.util.Collection;

import undercover.report.MethodMeasure;

public class CoverageDistribution {
	private int[] counts = new int[10];
	
	public CoverageDistribution(Collection<? extends MethodMeasure> items) {
		for (MethodMeasure each : items) {
			int i = (int) Math.floor(each.getCoverageRate() * 10);
			counts[i == 10 ? 9 : i]++;
		}
	}
	
	public int[] getCounts() {
		return counts;
	}
	
	public int getCountCeil() {
		int max = 0;
		for (int each : counts) {
			if (each > max) {
				max = each;
			}
		}
		return (int)(max * 1.1);
	}
}
