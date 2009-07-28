package undercover.report.html;

import java.util.Collection;

import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.support.xml.Element;

public class MenuPage extends HtmlPage {
	private ReportData reportData;

	public MenuPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public Element build() {
		return html().append(
				defaultHead("Undercover"),
				body().append(
						roundedBox(h2().append(text("Undercover Coverage Report"))),
						div().attr("class", "navigation").append(
								mainMenu(),
								h3().append(text("Packages")),
								packageList(reportData.getPackages()))));
	}
	
	Element mainMenu() {
		return ul().attr("class", "menu").append(
				li().append(a().attr("href", "project-dashboard.html").attr("target", "classPane").append(text("Dashboard"))),
				li().append(a().attr("href", "project-summary.html").attr("target", "classPane").append(text("Coverage"))));
	}
	
	Element packageList(Collection<PackageItem> packageItems) {
		Element result = ul().attr("class", "package-list");
		for (PackageItem each : packageItems) {
			String summaryPage = "package-" + each.getLinkName() + "-summary.html";
			result.append(li().append(
					a().attr("href", summaryPage).attr("target", "classPane").append(text(each.getDisplayName())),
					text(" ("), coveragePercent(each), text(")")));
		}
		return result;
	}	
}
