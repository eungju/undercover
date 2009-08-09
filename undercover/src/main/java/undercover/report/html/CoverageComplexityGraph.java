package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.support.xml.Element;

public class CoverageComplexityGraph extends HtmlPage {
	private final Collection<ClassItem> classItems;

	public CoverageComplexityGraph(Collection<ClassItem> classItems) {
		this.classItems = classItems;
	}
	
	@Override
	public Element build() {
		return div().attr("id", "coverage-complexity").append(
				div().attr("class", "graph").attr("style", "width: 340px; height: 280px;"),
				div().attr("class", "tooltip"),
				javascriptInline("drawCoverageComplexityGraph(\"#coverage-complexity div.graph\", \"#coverage-distribution .tooltip\", " + formatData(classItems) + ");")
				);
	}

	String formatData(Collection<ClassItem> classItems) {
		StringBuilder result = new StringBuilder("[");
		int i = 0;
		for (ClassItem each : classItems) {
			if (i != 0) {
				result.append(", ");
			}
			result.append('[')
				.append(String.format("%.1f", each.getBlockMetrics().getCoverage().getRatio() * 100)).append(',')
				.append(each.getBlockMetrics().getComplexity()).append(',')
				.append('"').append(each.getSimpleName()).append('"').append(',')
				.append('"').append("source-" + each.getSource().getLinkName() + ".html").append('"').append(']');
			i++;
		}
		return result.append(']').toString();
	}
}
