package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassMetrics;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.xml.Element;

public class ProjectSummaryPage extends HtmlPage {
	private ReportData reportData;

	public ProjectSummaryPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	@Override
	public Element build() {
		return html().append(
				defaultHead(reportData.getDisplayName()).append(loadClassListScript("project-classes.html")),
				body().append(
						navigationPanel(),
						itemStatisticsPanel(reportData),
						h3().append(text("Packages")),
						packageList(reportData.getPackages()),
						copyright()
						)
				);
	}
	
	Element packageList(Collection<PackageItem> items) {
		return table().attr("class", "item-children").append(
				colgroup().append(
						col().attr("width", "*"),
						col().attr("width", "80"),
						col().attr("width", "150"),
						col().attr("width", "150"),
						col().attr("width", "70"),
						col().attr("width", "60"),
						col().attr("width", "60")
						),
				thead().append(
						tr().append(
								th().append(text("Package")),
								th().append(text("Complexity")),
								th().append(text("Coverage")).attr("colspan", "2"),
								th().append(text("Classes")),
								th().append(text("Class Complexity (Avg.,Max.)")).attr("colspan", "2")
								)
						),
				packageListBody(items)
				);
	}
	
	Element packageListBody(Collection<PackageItem> items) {
		Element result = tbody();
		for (PackageItem each : items) {
			ClassMetrics classMetrics = each.getClassMetrics();
			result.append(tr().append(
					td().append(a().attr("href", "pakcage-" + each.getLinkName() + "-summary.html").append(text(each.getDisplayName()))),
					td().attr("class", "complexity").append(text(String.valueOf(each.getBlockMetrics().getComplexity()))),
					td().attr("class", "coverage").append(blockCoverage(each)),
					td().attr("class", "coverage").append(coverageBar(each)),
					td().attr("class", "number").append(text(String.valueOf(classMetrics.getCount()))),
					td().attr("class", "complexity").append(text(String.format("%.2f", classMetrics.getComplexity().getAverage()))),
					td().attr("class", "complexity").append(text(String.valueOf(classMetrics.getComplexity().getMaximum())))
					));
		}
		return result;
	}
}
