package undercover.report.html;

import undercover.support.xml.Element;

public class NavigationPanel extends HtmlPage {
	public Element build() {
		return div().append(
				a().attr("href", "project-dashboard.html").append(text("Dashboard")),
				a().attr("href", "project-summary.html").append(text("Overview")),
				a().attr("href", "index.html").attr("target", "_top").append(text("Frames")),
				a().attr("href", "#").attr("target", "_top").append(text("No Frames"))
				);		
	}
}
