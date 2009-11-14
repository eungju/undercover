package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.MethodMetrics;
import undercover.report.PackageItem;
import undercover.support.xml.Element;

public class PackageSummaryPage extends SummaryPage {
	private final PackageItem packageItem;

	public PackageSummaryPage(PackageItem packageItem) {
		this.packageItem = packageItem;
	}
	
	@Override
	public String getTitle() {
		return packageItem.getDisplayName();
	}
	
	@Override
	public Element getBody() {
		return body().append(
				new NavigationPanel().build(),
				new ItemStatisticsPanel(packageItem).build(),
				h3().append("Classes"),
				classList(packageItem.classes),
				new CopyrightPanel().build()
				);
	}
	
	@Override
	public String getClassListFrameUrl() {
		return "package-" + packageItem.getLinkName() + "-classes.html";
	};

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
								th().append("Class"),
								th().append("Classes"),
								th().append("Method Complexity (Avg.,Max.)").attr("colspan", "2"),
								th().append("Complexity"),
								th().append("Coverage").attr("colspan", "2")
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
					td().append(a().attr("href", "source-" + each.getSource().getLinkName() + ".html").append(each.getSimpleName())),
					td().attr("class", "number").append(String.valueOf(methodMetrics.getCount())),
					td().attr("class", "complexity").append(String.format("%.2f", methodMetrics.getComplexity().getAverage())),
					td().attr("class", "complexity").append(String.valueOf(methodMetrics.getComplexity().getMaximum())),
					td().attr("class", "complexity").append(String.valueOf(each.getBlockMetrics().getComplexity())),
					td().attr("class", "coverage").append(CoverageFormat.percentDetailed(each.getBlockMetrics().getCoverage())),
					td().attr("class", "coverage").append(new CoverageBar(each).build())
					));
		}
		return result;
	}
}
