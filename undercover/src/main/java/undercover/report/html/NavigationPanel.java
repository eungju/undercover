package undercover.report.html;

import static undercover.report.html.HtmlElements.*;
import undercover.support.xml.Element;

public class NavigationPanel implements HtmlFragment {
	public Element build() {
		return div().append(
				a().attr("href", "project-dashboard.html").append("Dashboard"),
				a().attr("href", "project-summary.html").append("Overview"),
				a().attr("href", "index.html").attr("target", "_top").append("Frames"),
				a().attr("href", "#").attr("target", "_top").append("No Frames")
				);		
	}
}
