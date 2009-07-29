package undercover.report.html;

import undercover.report.ReportData;
import undercover.support.xml.Element;

public class ProjectClassListPage extends ClassListPage {
	private ReportData reportData;

	public ProjectClassListPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	public Element build() {
		return html().append(
				defaultHead("All Classes"),
				body().append(classList(reportData.getClasses())
						)
				);
	}
}
