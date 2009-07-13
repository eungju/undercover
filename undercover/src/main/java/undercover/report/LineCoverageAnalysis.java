package undercover.report;

import java.util.HashMap;
import java.util.Map;

import undercover.data.BlockMeta;

public class LineCoverageAnalysis {
	private final Map<Integer, LineCoverage> lines = new HashMap<Integer, LineCoverage>();
	
	public void analyze(BlockMeta blockMeta, int blockCoverage) {
		for (Integer each : blockMeta.lines) {
			LineCoverage lineCoverage = lines.get(each);
			if (lineCoverage == null) {
				lineCoverage = new LineCoverage();
				lines.put(each, lineCoverage);
			}
			lineCoverage.addBlock(blockCoverage);
		}
	}

	public LineCoverage getLine(int lineNumber) {
		return lines.get(lineNumber);
	}
}
