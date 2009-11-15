package undercover.report.html;

import static undercover.report.html.HtmlElements.*;
import undercover.support.xml.Element;

/**
 * A panel which has rounded corners.
 */
public class RoundedPanel implements HtmlFragment {
	private final Element element;

	public RoundedPanel(Element element) {
		this.element = element;
	}

	public Element build() {
		return div().attr("class", "rounded-box")
		.append(div().attr("class", "round4"))
		.append(div().attr("class", "round2"))
		.append(div().attr("class", "round1"))
		.append(div().attr("class", "box-inner").append(element))
		.append(div().attr("class", "round1"))
		.append(div().attr("class", "round2"))
		.append(div().attr("class", "round4"));
	}
}
