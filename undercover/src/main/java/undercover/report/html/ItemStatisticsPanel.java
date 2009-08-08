package undercover.report.html;

import undercover.report.ComplexityStatistics;
import undercover.report.Item;
import undercover.support.xml.Element;

public class ItemStatisticsPanel extends HtmlPage {
	private final Item item;

	public ItemStatisticsPanel(Item item) {
		this.item = item;
	}
	
	public Element build() {
		return div().append(
				h1().append(text(item.getDisplayName())),
				roundedBox(createContent(item))
				);
	}
	
	Element createContent(Item item) {
		return table().attr("class", "item-statistics").append(
				colgroup().append(
						col().attr("width", "300"),
						col().attr("width", "*")
						),
				tbody().append(
						tr().append(
								td().append(createBriefView(item)),
								td().attr("class", "vertical-separator"),
								td().append(createDetailView(item))
								)
						)
				);
	}

	Element createBriefView(Item item) {
		return table().attr("width", "100%").append(
				colgroup().append(
						col().attr("width", "80"),
						col().attr("width", "*")
						),
				tbody().append(
						tr().append(
								th().append(text("Complexity")),
								td().append(text(String.valueOf(item.getBlockMetrics().getComplexity())))
								),
						tr().append(
								th().append(text("Coverage")),
								td().append(blockCoverage(item))
								),
						tr().append(
								td().attr("colspan", "2").append(new CoverageBar(item).build())
								)
						)
				);
	}

	Element createDetailView(Item item) {
		Element tbody = tbody().append(
				tr().append(
						th(),
						th().append(text("Count")),
						th().append(text("Avg. Complexity")),
						th().append(text("Max. Complexity")),
						th().append(text("S.D. of Complexity"))
						)
				);
		if (item.getPackageMetrics() != null) {
			tbody.append(itemStatisticsMetrics("Packages", item.getPackageMetrics().getCount(), item.getPackageMetrics().getComplexity()));
		}
		if (item.getClassMetrics() != null) {
			tbody.append(itemStatisticsMetrics("Classes", item.getClassMetrics().getCount(), item.getClassMetrics().getComplexity()));
		}
		tbody.append(itemStatisticsMetrics("Methods", item.getMethodMetrics().getCount(), item.getMethodMetrics().getComplexity()));
		return table().append(tbody);
	}
	
	Element itemStatisticsMetrics(String name, int count, ComplexityStatistics complexity) {
		return tr().append(
				th().append(text(name)),
				td().attr("class", "number").append(text(String.valueOf(count))),
				td().attr("class", "complexity").append(text(String.format("%.2f", complexity.getAverage()))),
				td().attr("class", "complexity").append(text(String.valueOf(complexity.getMaximum()))),
				td().attr("class", "complexity").append(text(String.format("%.2f", complexity.getStandardDeviation())))
				);
	}
}
