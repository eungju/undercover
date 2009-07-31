package undercover.report.html;

import java.util.Collection;

import undercover.report.ClassItem;
import undercover.report.MethodItem;
import undercover.report.MethodMetrics;
import undercover.report.SourceItem;
import undercover.report.SourceLine;
import undercover.support.xml.Element;

public class SourceSummaryPage extends HtmlPage {
	private SourceItem sourceItem;

	public SourceSummaryPage(SourceItem sourceItem) {
		this.sourceItem = sourceItem;
	}
	
	@Override
	public Element build() {
		return html().append(
				defaultHead(sourceItem.getDisplayName()),
				body().append(
						navigationPanel(),
						itemStatisticsPanel(sourceItem),
						h3().append(text("Classes")),
						classList(sourceItem.classes),
						h3().append(text("Source")),
						sourceView(sourceItem),
						copyright()
						)
				);
	}

	Element classList(Collection<ClassItem> items) {
		return table().attr("class", "item-children").append(
				colgroup().append(
						col().attr("width", "*"),
						col().attr("width", "70"),
						col().attr("width", "60"),
						col().attr("width", "60"),
						col().attr("width", "80"),
						col().attr("width", "100"),
						col().attr("width", "150")
						),
				thead().append(
						tr().append(
								th().append(text("Class")),
								th().append(text("Methods")),
								th().append(text("Method Complexity (Avg.,Max.)")).attr("colspan", "2"),
								th().append(text("Complexity")),
								th().append(text("Coverage")).attr("colspan", "2")
								)
						),
				classListBody(items)
				);
	}
	
	Element classListBody(Collection<ClassItem> items) {
		Element result = tbody();
		for (ClassItem each : items) {
			MethodMetrics methodMetrics = each.getMethodMetrics();
			result.append(tr().append(
					th().append(text(each.getSimpleName())),
					td().attr("class", "number").append(text(String.valueOf(methodMetrics.getCount()))),
					td().attr("class", "complexity").append(text(String.format("%.2f", methodMetrics.getComplexity().getAverage()))),
					td().attr("class", "complexity").append(text(String.valueOf(methodMetrics.getComplexity().getMaximum()))),
					td().attr("class", "complexity").append(text(String.valueOf(each.getBlockMetrics().getComplexity()))),
					td().attr("class", "coverage").append(blockCoverage(each)),
					td().attr("class", "coverage").append(coverageBar(each))
					));
			for (MethodItem methodItem : each.methods) {
				result.append(tr().append(
						td().attr("colspan", "4").append(text(methodItem.getDisplayName())),
						td().attr("class", "complexity").append(text(String.valueOf(methodItem.getBlockMetrics().getComplexity()))),
						td().attr("class", "coverage").append(blockCoverage(methodItem)),
						td().attr("class", "coverage").append(coverageBar(methodItem))
						));
			}
		}
		return result;
	}

	Element sourceView(SourceItem sourceItem) {
		Element tbody = tbody();
		for (SourceLine each : sourceItem.getLines()) {
			Element tr = tr();
			if (each.isExecutable()) {
				String styleClass = null;
				if (each.coverage.isCompletelyCovered()) {
					styleClass = "completely-covered";
				} else if (each.coverage.isPartialyCovered()) {
					styleClass = "partialy-covered";
				} else {
					styleClass = "not-covered";
				}
				tr.attr("class", styleClass);
			}
			tr.append(
					td().attr("class", "line-number").append(text(String.valueOf(each.number))),
					td().attr("class", "line-touch").append(text(each.isExecutable() ? String.valueOf(each.coverage.touchCount) : "")),
					td().attr("class", "line-text").append(text(each.text))
					);
			tbody.append(tr);
		}
		return table().attr("class", "source").append(
				colgroup().append(
						col().attr("width", "60"),
						col().attr("width", "40"),
						col().attr("width", "*")
						),
				tbody
				);
	}
	
//	<table class="source">
//	<colgroup>
//		<col width="60" />
//		<col width="40" />
//		<col width="*" />
//	</colgroup>
//	<tbody>
//	$source.lines:{ each |
//		<tr$if(each.executable)$ class="$if(each.coverage.completelyCovered)$completely$elseif(each.coverage.partialyCovered)$partialy$else$not$endif$-covered"$endif$>
//			<td class="line-number">$each.number$</td>
//			<td class="line-touch">$if(each.executable)$$each.coverage.touchCount$$endif$</td>
//			<td class="line-text">$each.text:html()$</td>
//		</tr>
//	}$
//	</tbody>
//</table>

}
