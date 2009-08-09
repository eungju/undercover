package undercover.report.html;

import java.util.Collections;

import org.junit.Test;

import undercover.report.ClassItem;
import undercover.report.PackageItem;
import undercover.report.ReportData;
import undercover.report.SourceItem;
import undercover.support.xml.Element;

public class ProjectSummaryPageTest {
	@Test public void build() {
		ReportData reportData = new ReportData("name", Collections.<PackageItem>emptyList(), Collections.<ClassItem>emptyList(), Collections.<SourceItem>emptyList());
		ProjectSummaryPage dut = new ProjectSummaryPage(reportData);
		Element actual = dut.build();
	}
}
