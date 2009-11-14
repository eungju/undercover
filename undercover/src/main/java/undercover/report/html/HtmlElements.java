package undercover.report.html;

import undercover.support.xml.Cdata;
import undercover.support.xml.Element;
import undercover.support.xml.Text;

public class HtmlElements {
	public static Element html() {
		return new Element("html");
	}
	
	public static Element body() {
		return new Element("body");
	}
	
	public static Element head() {
		return new Element("head");
	}

	public static Element title() {
		return new Element("title");
	}
	
	public static Element meta() {
		return new Element("meta");		
	}

	public static Element link() {
		return new Element("link");
	}

	public static Element script() {
		return new Element("script");
	}

	public static Element div() {
		return new Element("div");
	}
	
	public static Element h1() {
		return new Element("h1");
	}

	public static Element h2() {
		return new Element("h2");
	}

	public static Element h3() {
		return new Element("h3");
	}
	
	public static Element ul() {
		return new Element("ul");
	}
	
	public static Element li() {
		return new Element("li");
	}

	public static Element table() {
		return new Element("table");
	}
	
	public static Element colgroup() {
		return new Element("colgroup");
	}

	public static Element col() {
		return new Element("col");
	}

	public static Element thead() {
		return new Element("thead");
	}

	public static Element tbody() {
		return new Element("tbody");
	}

	public static Element tr() {
		return new Element("tr");
	}
	
	public static Element th() {
		return new Element("th");
	}
	
	public static Element td() {
		return new Element("td");
	}
	
	public static Element p() {
		return new Element("p");
	}

	public static Element a() {
		return new Element("a");
	}

	public static Element javascript(String src) {
		return script().attr("src", src).attr("type", "text/javascript");
	}

	public static Element javascriptInline(String code) {
		return new Element("script").attr("type", "text/javascript").append(
				new Text("//"),
				new Cdata("\n" + code + "\n//")
				);
	}

	public static Element css(String href) {
		return link().attr("href", href).attr("rel", "stylesheet").attr("type", "text/css");
	}
}
