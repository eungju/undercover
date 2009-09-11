package undercover.report.html;

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
	
	public Element p() {
		return new Element("p");
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

	public Element javascriptInline(String code) {
		return new Element("script").attr("type", "text/javascript").append(
				text("//"),
				new Cdata("\n" + code + "\n//")
				);
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
			.append(new Comment("[if IE]><script src=\"excanvas.js\" type=\"text/javascript\"></script><![endif]"))
			.append(javascript("jquery.flot.js"))
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

	String formatAsPercent(double value) {
		return String.format("%.1f", value * 100).replaceFirst("\\.?0+$", "") + "%";
	}
	
	protected Text coveragePercent(Item item) {
		String percent = formatAsPercent(item.getBlockMetrics().getCoverage().getRatio());
		return text(item.getBlockMetrics().isExecutable() ? percent : "N/A");
	}

	protected Text blockCoverage(Item item) {
		if (!item.getBlockMetrics().isExecutable()) {
			return text("N/A");
		}
		Proportion coverage = item.getBlockMetrics().getCoverage();
		return text(formatAsPercent(coverage.getRatio()) + String.format(" (%d/%d)", coverage.part, coverage.whole));
	}

	protected Element loadClassListScript(String path) {
		return javascriptInline("$(document).ready(function() { parent.classListPane.location = \"" + path + "\"; });");
	}

	public abstract Element build();
}
