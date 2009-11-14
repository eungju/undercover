package undercover.report.html;

import undercover.support.xml.Comment;
import undercover.support.xml.Element;

public abstract class SummaryPage extends ReportPage {
	@Override
	public Element getHead() {
		Element head = super.getHead();
		head.append(
				javascript("jquery-1.3.2.min.js"),
				new Comment("[if IE]><script src=\"excanvas.js\" type=\"text/javascript\"></script><![endif]"),
				javascript("jquery.flot.js"),
				javascript("undercover.js")
				);
		if (getClassListFrameUrl() != null) {
			head.append(loadClassListScript(getClassListFrameUrl()));
		}
		return head;
	}
	
	protected Element loadClassListScript(String path) {
		return javascriptInline("$(document).ready(function() { if (parent.classListPane) parent.classListPane.location = \"" + path + "\"; });");
	}
	
	public String getClassListFrameUrl() {
		return null;
	};
}
