package undercover.report.html;

import static undercover.report.html.HtmlElements.*;
import undercover.report.ComplexityStatistics;
import undercover.report.Item;
import undercover.support.xml.Element;

public class ItemStatisticsPanel implements HtmlFragment {
	private final Item item;

	public ItemStatisticsPanel(Item item) {
		this.item = item;
	}
	
	public Element build() {
		return div().append(
				h1().append(item.getDisplayName()),
				new RoundedPanel(createContent(item)).build()
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
								th().append("Complexity"),
								td().append(String.valueOf(item.getBlockMetrics().getComplexity()))
								),
						tr().append(
								th().append("Coverage"),
								td().append(CoverageFormat.percentDetailed(item.getBlockMetrics().getCoverage()))
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
						th().append("Count"),
						th().append("Avg. Complexity"),
						th().append("Max. Complexity"),
						th().append("S.D. of Complexity")
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
				th().append(name),
				td().attr("class", "number").append(String.valueOf(count)),
				td().attr("class", "complexity").append(String.format("%.2f", complexity.getAverage())),
				td().attr("class", "complexity").append(String.valueOf(complexity.getMaximum())),
				td().attr("class", "complexity").append(String.format("%.2f", complexity.getStandardDeviation()))
				);
	}
}
