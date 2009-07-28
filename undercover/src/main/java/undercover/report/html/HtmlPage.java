package undercover.report.html;

import undercover.report.Item;
import undercover.report.ReportData;
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

	public Element a() {
		return new Element("a");
	}
	
	public Text text(String value) {
		return new Text(value);
	}

	protected Element defaultHead(String title) {
		return head()
			.append(title().append(text(title)))
			.append(link().attr("rel", "stylesheet").attr("type", "text/css").attr("href", "style.css"))
			.append(script().attr("src", "jquery-1.3.2.min.js").attr("type", "text/javascript"))
			.append(new Comment("[if IE]><script src=\"excanvas.pack.js\" type=\"text/javascript\"></script><![endif]"))
			.append(script().attr("src", "jquery.flot.pack.js").attr("type", "text/javascript"))
			.append(script().attr("src", "undercover.js").attr("type", "text/javascript"));
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
	
	public abstract Element build(ReportData reportData);
}
