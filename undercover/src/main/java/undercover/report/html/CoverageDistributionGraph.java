package undercover.report.html;

import static undercover.report.html.HtmlElements.*;

import java.util.Collection;

import undercover.report.Item;
import undercover.support.xml.Element;

public class CoverageDistributionGraph implements HtmlFragment {
	private final Collection<? extends Item> items;

	public CoverageDistributionGraph(Collection<? extends Item> items) {
		this.items = items;
	}
	
	public Element build() {
		int[] counts = new int[10];
		for (Item each : items) {
			counts[coverageInterval(each.getBlockMetrics().getCoverage().getRatio())]++;
		}
		return div().attr("id", "coverage-distribution").append(
				div().attr("class", "graph").attr("style", "width: 340px; height: 280px;"),
				div().attr("class", "tooltip"),
				javascriptInline("drawCoverageDistributionGraph(\"#coverage-distribution div.graph\", \"#coverage-distribution .tooltip\", " + formatData(counts) + ");")
				);
	}

	String formatData(int[] counts) {
		StringBuilder result = new StringBuilder("[");
		int i = 0;
		for (int each : counts) {
			if (i != 0) {
				result.append(", ");
			}
			result.append('[').append(i * 10).append(',').append(each).append(']');
			i++;
		}
		return result.append(']').toString();
	}
	
	int coverageInterval(double coverageRate) {
		int i = (int) Math.floor(coverageRate * 10);
		return i == 10 ? 9 : i;
	}
}
