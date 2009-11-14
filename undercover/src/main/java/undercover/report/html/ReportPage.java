package undercover.report.html;

import undercover.support.xml.Element;

public abstract class ReportPage extends HtmlElements implements HtmlFragment {
	public Element build() {
		return html().append(getHead(), getBody());
	}
	
	public Element getHead() {
		return head().append(
				title().append(getTitle()),
				css("style.css")
				);		
	}
	
	public abstract String getTitle();
	
	public abstract Element getBody();
}
