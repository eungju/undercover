package undercover.report.html;

import undercover.support.xml.Element;

public class CopyrightPanel extends HtmlPage {
	public Element build() {
		return div().attr("class", "copyright").append(
				p().append(
						a().attr("href", "http://code.google.com/p/undercover/").attr("target", "_top").append(text("Undercover")),
						text(" | Copyright © 2009 Eung-ju PARK. All rights reserved. Licensed under the "),
						a().attr("href", "http://www.apache.org/licenses/LICENSE-2.0").attr("target", "_top").append(text("Apache License, Version 2.0"))
						)
				);
	}
}
