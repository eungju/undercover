package undercover.report.html;

import undercover.support.xml.Element;

/**
 * A report HTML page.
 */
public abstract class ReportPage extends HtmlElements implements HtmlFragment {
	public Element build() {
		return html().append(getHead(), getBody());
	}

	/**
	 * Head of this page.
	 */
	public Element getHead() {
		return head().append(
				title().append(getTitle()),
				css("style.css")
				);		
	}
	
	/**
	 * Title of this page.
	 */
	public abstract String getTitle();

	/**
	 * Body of this page.
	 */
	public abstract Element getBody();
}
