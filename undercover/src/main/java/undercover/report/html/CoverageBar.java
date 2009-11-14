package undercover.report.html;

import static undercover.report.html.HtmlElements.*;
import undercover.report.Item;
import undercover.support.xml.Element;

public class CoverageBar implements HtmlFragment {
	private final Item item;

	public CoverageBar(Item item) {
		this.item = item;
	}
	
	public Element build() {
		Element result = div().attr("class", "coverage-bar");
		if (!item.getBlockMetrics().isExecutable()) {
			result.append(div().attr("class", "not-available"));
			return result;
		}
		return result.append(div().attr("class", "negative").append(
				div().attr("class", "positive").attr("style", String.format("width: %.1f%%;", item.getBlockMetrics().getCoverage().getRatio() * 100))
				));
	}
}
