package undercover.report.html;

import undercover.report.ComplexityStatistics;
import undercover.report.Item;
import undercover.support.Proportion;
import undercover.support.xml.Cdata;
import undercover.support.xml.Comment;
import undercover.support.xml.Element;
import undercover.support.xml.Text;

public abstract class HtmlPage {
	public Element html() {
		return new Element("html");
	}
	
	public Element body() {
		return new Element("body");
	}
	
	public Element head() {
		return new Element("head");
	}

	public Element title() {
		return new Element("title");
	}

	public Element link() {
		return new Element("link");
	}

	public Element script() {
		return new Element("script");
	}

	public Element div() {
		return new Element("div");
	}
	
	public Element h1() {
		return new Element("h1");
	}

	public Element h2() {
		return new Element("h2");
	}

	public Element h3() {
		return new Element("h3");
	}
	
	public Element ul() {
		return new Element("ul");
	}
	
	public Element li() {
		return new Element("li");
	}

	public Element table() {
		return new Element("table");
	}
	
	public Element colgroup() {
		return new Element("colgroup");
	}

	public Element col() {
		return new Element("col");
	}

	public Element thead() {
		return new Element("thead");
	}

	public Element tbody() {
		return new Element("tbody");
	}

	public Element tr() {
		return new Element("tr");
	}
	
	public Element th() {
		return new Element("th");
	}
	
	public Element td() {
		return new Element("td");
	}

	public Element a() {
		return new Element("a");
	}

	public Text text(String value) {
		return new Text(value);
	}
	
	public Element javascript(String src) {
		return script().attr("src", src).attr("type", "text/javascript");
	}

	public Element css(String href) {
		return link().attr("href", href).attr("rel", "stylesheet").attr("type", "text/css");
	}

	protected Element defaultHead(String title) {
		return head()
			.append(title().append(text(title)))
			.append(new Element("meta").attr("http-equiv", "Content-Style-Type").attr("content", "text/css"))
			.append(css("style.css"))
			.append(javascript("jquery-1.3.2.min.js"))
			.append(new Comment("[if IE]><script src=\"excanvas.pack.js\" type=\"text/javascript\"></script><![endif]"))
			.append(javascript("jquery.flot.pack.js"))
			.append(javascript("undercover.js"));
	}

	protected Element roundedBox(Element innerBox) {
		return div().attr("class", "rounded-box")
			.append(div().attr("class", "round4"))
			.append(div().attr("class", "round2"))
			.append(div().attr("class", "round1"))
			.append(div().attr("class", "box-inner").append(innerBox))
			.append(div().attr("class", "round1"))
			.append(div().attr("class", "round2"))
			.append(div().attr("class", "round4"));
	}
	
	protected Text coveragePercent(Item item) {
		String percent = DoubleRenderer.trimZeros(String.format("%.1f", item.getBlockMetrics().getCoverage().getRatio() * 100)) + "%";
		return text(item.getBlockMetrics().isExecutable() ? percent : "N/A");
	}

	protected Text blockCoverage(Item item) {
		if (!item.getBlockMetrics().isExecutable()) {
			return text("N/A");
		}
		Proportion coverage = item.getBlockMetrics().getCoverage();
		return text(DoubleRenderer.trimZeros(String.format("%.1f", coverage.getRatio() * 100)) + "%" + String.format(" (%d/%d)", coverage.part, coverage.whole));
	}

	protected Element coverageBar(Item item) {
		Element result = div().attr("class", "coverageBar");
		if (!item.getBlockMetrics().isExecutable()) {
			result.append(div().attr("class", "coverageBarNotAvailable"));
			return result;
		}
		return result.append(div().attr("class", "coverageBarNegative").append(
				div().attr("class", "coverageBarPositive").attr("style", String.format("width: %.1f%%;", item.getBlockMetrics().getCoverage().getRatio() * 100))
				));
	}
	
	protected Element navigationPanel() {
		return div().append(
				a().attr("href", "project-dashboard.html").append(text("Dashboard")),
				a().attr("href", "project-summary.html").append(text("Overview")),
				a().attr("href", "index.html").attr("target", "_top").append(text("Frames")),
				a().attr("href", "#").attr("target", "_top").append(text("No Frames"))
				);		
	}
	
	protected Element itemStatisticsPanel(Item item) {
		return div().append(
				h1().append(text(item.getDisplayName())),
				roundedBox(itemStatistics(item))
				);
	}
	
	Element itemStatistics(Item item) {
		return table().attr("class", "item-statistics").append(
				colgroup().append(
						col().attr("width", "300"),
						col().attr("width", "*")
						),
				tbody().append(
						tr().append(
								td().append(itemStatisticsBrief(item)),
								td().attr("style", "border-left: 1px solid #999;"),
								td().append(itemStatisticsDetail(item))
								)
						)
				);
	}

	Element itemStatisticsBrief(Item item) {
		return table().attr("width", "100%").append(
				colgroup().append(
						col().attr("width", "80"),
						col().attr("width", "*")
						),
				tbody().append(
						tr().append(
								th().append(text("Complexity")),
								td().append(text(String.valueOf(item.getBlockMetrics().getComplexity())))
								),
						tr().append(
								th().append(text("Coverage")),
								td().append(blockCoverage(item))
								),
						tr().append(
								td().attr("colspan", "2").append(coverageBar(item))
								)
						)
				);
	}

	Element itemStatisticsDetail(Item item) {
		Element tbody = tbody().append(
				tr().append(
						th(),
						th().append(text("Count")),
						th().append(text("Avg. Complexity")),
						th().append(text("Max. Complexity")),
						th().append(text("S.D. of Complexity"))
						)
				);
		if (item.getPackageMetrics() != null) {
			tbody.append(itemStatisticsMetrics("Packages", item.getPackageMetrics().getCount(), item.getPackageMetrics().getComplexity()));
		}
		if (item.getClassMetrics() != null) {
			tbody.append(itemStatisticsMetrics("Classes", item.getClassMetrics().getCount(), item.getClassMetrics().getComplexity()));
		}
		tbody.append(itemStatisticsMetrics("Methods", item.getMethodMetrics().getCount(), item.getMethodMetrics().getComplexity()));
		return table().append(tbody);
	}
	
	Element itemStatisticsMetrics(String name, int count, ComplexityStatistics complexity) {
		return tr().append(
				th().append(text(name)),
				td().attr("class", "number").append(text(String.valueOf(count))),
				td().attr("class", "complexity").append(text(String.format("%.2f", complexity.getAverage()))),
				td().attr("class", "complexity").append(text(String.valueOf(complexity.getMaximum()))),
				td().attr("class", "complexity").append(text(String.format("%.2f", complexity.getStandardDeviation())))
				);
	}

	public Element p() {
		return new Element("p");
	}
	
	protected Element copyright() {
		return div().attr("class", "copyright").append(
				p().append(
						a().attr("href", "http://code.google.com/p/undercover/").attr("target", "_top").append(text("Undercover")),
						text(" | Copyright © 2009 Eung-ju PARK. All rights reserved. Licensed under the "),
						a().attr("href", "http://www.apache.org/licenses/LICENSE-2.0").attr("target", "_top").append(text("Apache License, Version 2.0"))
						)
				);
	}

	protected Element loadClassListScript(String path) {
		return new Element("script").attr("type", "text/javascript").append(
				text("//"),
				new Cdata("\n$(document).ready(function() { parent.classListPane.location = \"" + path + "\"; });\n//")
				);
	}

	public abstract Element build();
}
