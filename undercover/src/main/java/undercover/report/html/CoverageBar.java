package undercover.report.html;

import undercover.report.Item;
import undercover.support.xml.Element;

public class CoverageBar extends HtmlPage {
	private final Item item;

	public CoverageBar(Item item) {
		this.item = item;
	}
	
	public Element build() {
		Element result = div().attr("class", "coverageBar");
		if (!item.getBlockMetrics().isExecutable()) {
			result.append(div().attr("class", "coverageBarNotAvailable"));
			return result;
		}
		return result.append(div().attr("class", "coverageBarNegative").append(
				div().attr("class", "coverageBarPositive").attr("style", String.format("width: %.1f%%;", item.getBlockMetrics().getCoverage().getRatio() * 100))
				));
	}
}
