package undercover.report.html;

import undercover.support.Proportion;

public class CoverageFormat {
	static String formatAsPercent(double value) {
		return String.format("%.1f", value * 100).replaceFirst("\\.?0+$", "") + "%";
	}
	
	public static String percentShort(Proportion coverage) {
		if (coverage.whole == 0) {
			return "N/A";
		}
		return formatAsPercent(coverage.getRatio());
	}
	
	public static String percentDetailed(Proportion coverage) {
		if (coverage.whole == 0) {
			return "N/A";
		}
		return formatAsPercent(coverage.getRatio()) + String.format(" (%d/%d)", coverage.part, coverage.whole);
	}
}
