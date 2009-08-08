package undercover.report.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import undercover.report.ClassItem;
import undercover.report.Item;
import undercover.report.ReportData;
import undercover.support.xml.Element;

public class DashboardPage extends HtmlPage {
	private ReportData reportData;

	public DashboardPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	@Override
	public Element build() {
		return html().append(
				defaultHead(reportData.getDisplayName()).append(loadClassListScript("project-classes.html")),
				body().append(
						navigationPanel(),
						itemStatisticsPanel(reportData),
						table().attr("class", "layout").append(
								colgroup().append(
										col().attr("width", "*"),
										col().attr("width", "40"),
										col().attr("width", "*")
										),
								tbody().append(
										tr().append(
												td().append(
														h3().append(text("Coverage Distribution")),
														div().attr("id", "coverage-distribution").append(
																div().attr("class", "graph").attr("style", "width: 340px; height: 280px;"),
																div().attr("class", "tooltip"),
																javascriptInline("drawCoverageDistributionGraph(\"#coverage-distribution div.graph\", \"#coverage-distribution .tooltip\", " + formatCoverageDistributionData(new CoverageDistribution(reportData.getClasses())) + ");")
																)
														),
												td(),
												td().append(
														h3().append(text("Coverage-Complexity")),
														div().attr("id", "coverage-complexity").append(
																div().attr("class", "graph").attr("style", "width: 340px; height: 280px;"),
																div().attr("class", "tooltip"),
																javascriptInline("drawCoverageComplexityGraph(\"#coverage-complexity div.graph\", \"#coverage-distribution .tooltip\", " + formatCoverageComplexityData(new CoverageComplexity(reportData.getClasses())) + ");")
																)
														)
												),
										tr().append(
												td().append(
														h3().append(text("Heaviest Classes")),
														classRankingTable(mostComplex(reportData.getClasses(), 10)),
														h3().append(text("Least Exercised Classes")),
														classRankingTable(leastCovered(reportData.getClasses(), 10))
														),
												td(),
												td().append(
														h3().append(text("Weakest Classes")),
														classRankingTable(mostRisky(reportData.getClasses(), 20))
														)
												)
										)
								),
						copyright()
						)
				);
	}
	
	String formatCoverageDistributionData(CoverageDistribution coverageDistribution) {
		StringBuilder result = new StringBuilder("[");
		int i = 0;
		for (int each : coverageDistribution.getCounts()) {
			if (i != 0) {
				result.append(", ");
			}
			result.append('[').append(i * 10).append(',').append(each).append(']');
			i++;
		}
		return result.append(']').toString();
	}
	
	String formatCoverageComplexityData(CoverageComplexity coverageComplexity) {
		StringBuilder result = new StringBuilder("[");
		int i = 0;
		for (ClassItem each : coverageComplexity.getItems()) {
			if (i != 0) {
				result.append(", ");
			}
			result.append('[')
				.append(each.getBlockMetrics().getCoverage().getRatio() * 100).append(',')
				.append(each.getBlockMetrics().getComplexity()).append(',')
				.append('"').append(each.getSimpleName()).append('"').append(',')
				.append('"').append("source-" + each.getSource().getLinkName() + ".html").append('"').append(']');
			i++;
		}
		return result.append(']').toString();
	}

	Element classRankingTable(Collection<ClassItem> items) {
		Element tableBody = tbody();
		int rank = 1;
		for (ClassItem each : items) {
			tableBody.append(tr().append(
					td().attr("class", "number").append(text(String.format("%d.", rank))),
					td().attr("class", "coverage").append(coveragePercent(each)),
					td().attr("class", "coverage").append(coverageBar(each)),
					td().append(a().attr("href", "source-" + each.getSource().getLinkName() + ".html").append(text(each.getSimpleName())))
					));
			rank++;
		}
		return table().append(
				colgroup().append(
						col().attr("width", "20"),
						col().attr("width", "50"),
						col().attr("width", "60"),
						col().attr("width", "*")
						),
				tableBody
				);
	}

	<T extends Item> List<T> takeTopN(Collection<T> candidates, Comparator<T> comparator, int max) {
		List<T> items = new ArrayList<T>(candidates);
		Collections.sort(items, comparator);
		if (items.size() > max) {
			items = items.subList(0, max);
		}
		return items;
	}

	public <T extends Item> List<T> mostRisky(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return (int) Math.signum((b.getBlockMetrics().getRisk() - a.getBlockMetrics().getRisk()));
			}
		}, max);
	}

	public <T extends Item> List<T> mostComplex(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return b.getBlockMetrics().getComplexity() - a.getBlockMetrics().getComplexity();
			}
		}, max);
	}

	public <T extends Item> List<T> leastCovered(Collection<T> candidates, int max) {
		return takeTopN(candidates, new Comparator<T>() {
			public int compare(T a, T b) {
				return (int) Math.signum(a.getBlockMetrics().getCoverage().getRatio() - b.getBlockMetrics().getCoverage().getRatio());
			}
		}, max);
	}
}
