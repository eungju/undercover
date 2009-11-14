package undercover.report.html;

import java.util.Collection;

import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.xml.Element;

public class MenuPage extends ReportPage {
	private final ReportData reportData;

	public MenuPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	@Override
	public String getTitle() {
		return "Undercover Coverage Report";
	}
	
	@Override
	public Element getBody() {
		return body().append(
				new RoundedPanel(h2().append("Undercover Coverage Report")).build(),
				div().attr("class", "navigation").append(
						mainMenu(),
						h3().append("Packages"),
						packageList(reportData.getPackages())
						)
				);
	}
	
	Element mainMenu() {
		return ul().attr("class", "menu").append(
				li().append(a().attr("href", "project-dashboard.html").attr("target", "classPane").append("Dashboard")),
				li().append(a().attr("href", "project-summary.html").attr("target", "classPane").append("Overview")));
	}
	
	Element packageList(Collection<PackageItem> packageItems) {
		Element result = ul().attr("class", "package-list");
		for (PackageItem each : packageItems) {
			String summaryPage = "package-" + each.getLinkName() + "-summary.html";
			result.append(li()
					.append(a().attr("href", summaryPage).attr("target", "classPane").append(each.getDisplayName()))
					.append(" (" + CoverageFormat.percentShort(each.getBlockMetrics().getCoverage()) + ")"));
		}
		return result;
	}	
}
