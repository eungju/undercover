package undercover.report.html;

import undercover.report.LineCoverage;

public class SourceLine {
	public final int number;
	public final String text;
	public final LineCoverage coverage;
	
	public SourceLine(int number, String text, LineCoverage coverage) {
		this.number = number;
		this.text = text;
		this.coverage = coverage;
	}
	
	public boolean isExecutable() {
		return coverage != null;
	}
}