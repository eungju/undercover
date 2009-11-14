package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.ReportData;

public class ProjectClassListPage extends ClassListPage {
	private final ReportData reportData;

	public ProjectClassListPage(ReportData reportData) {
		this.reportData = reportData;
	}
	
	@Override
	public String getTitle() {
		return "All Classes";
	}
	
	@Override
	public Collection<ClassItem> getClassItems() {
		return reportData.getClasses();
	}
}
