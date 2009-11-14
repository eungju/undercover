package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassMetrics;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.xml.Element;

public class ProjectSummaryPage extends SummaryPage {
	private ReportData reportData;

	public ProjectSummaryPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public Element build() {
		return html().append(
				defaultHead(reportData.getDisplayName()).append(loadClassListScript("project-classes.html")),
				body().append(
						new NavigationPanel().build(),
						new ItemStatisticsPanel(reportData).build(),
						h3().append("Packages"),
						packageList(reportData.getPackages()),
						new CopyrightPanel().build()
						)
				);
	}
	
	Element packageList(Collection<PackageItem> items) {
		return table().attr("class", "item-children").append(
				colgroup().append(
						col().attr("width", "*"),
						col().attr("width", "70"),
						col().attr("width", "60"),
						col().attr("width", "60"),
						col().attr("width", "80"),
						col().attr("width", "120"),
						col().attr("width", "150")
						),
				thead().append(
						tr().append(
								th().append("Package"),
								th().append("Classes"),
								th().append("Class Complexity (Avg.,Max.)").attr("colspan", "2"),
								th().append("Complexity"),
								th().append("Coverage").attr("colspan", "2")
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
					td().append(a().attr("href", "package-" + each.getLinkName() + "-summary.html").append(each.getDisplayName())),
					td().attr("class", "number").append(String.valueOf(classMetrics.getCount())),
					td().attr("class", "complexity").append(String.format("%.2f", classMetrics.getComplexity().getAverage())),
					td().attr("class", "complexity").append(String.valueOf(classMetrics.getComplexity().getMaximum())),
					td().attr("class", "complexity").append(String.valueOf(each.getBlockMetrics().getComplexity())),
					td().attr("class", "coverage").append(CoverageFormat.percentDetailed(each.getBlockMetrics().getCoverage())),
					td().attr("class", "coverage").append(new CoverageBar(each).build())
					));
		}
		return result;
	}
}
