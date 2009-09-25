package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.MethodMetrics;
import undercover.report.PackageItem;
import undercover.support.xml.Element;

public class PackageSummaryPage extends HtmlPage {
	private PackageItem packageItem;

	public PackageSummaryPage(PackageItem packageItem) {
		this.packageItem = packageItem;
	}
	
	@Override
	public Element build() {
		return html().append(
				defaultHead(packageItem.getDisplayName()).append(loadClassListScript("package-" + packageItem.getLinkName() + "-classes.html")),
				body().append(
						new NavigationPanel().build(),
						new ItemStatisticsPanel(packageItem).build(),
						h3().append(text("Classes")),
						classList(packageItem.classes),
						new CopyrightPanel().build()
						)
				);
	}
	
	Element classList(Collection<ClassItem> items) {
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
								th().append(text("Class")),
								th().append(text("Classes")),
								th().append(text("Method Complexity (Avg.,Max.)")).attr("colspan", "2"),
								th().append(text("Complexity")),
								th().append(text("Coverage")).attr("colspan", "2")
								)
						),
				classListBody(items)
				);
	}
	
	Element classListBody(Collection<ClassItem> items) {
		Element result = tbody();
		for (ClassItem each : items) {
			MethodMetrics methodMetrics = each.getMethodMetrics();
			result.append(tr().append(
					td().append(a().attr("href", "source-" + each.getSource().getLinkName() + ".html").append(text(each.getSimpleName()))),
					td().attr("class", "number").append(text(String.valueOf(methodMetrics.getCount()))),
					td().attr("class", "complexity").append(text(String.format("%.2f", methodMetrics.getComplexity().getAverage()))),
					td().attr("class", "complexity").append(text(String.valueOf(methodMetrics.getComplexity().getMaximum()))),
					td().attr("class", "complexity").append(text(String.valueOf(each.getBlockMetrics().getComplexity()))),
					td().attr("class", "coverage").append(blockCoverage(each)),
					td().attr("class", "coverage").append(new CoverageBar(each).build())
					));
		}
		return result;
	}
}
